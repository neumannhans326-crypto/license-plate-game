package com.kennzeichen.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Material3
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kennzeichen.app.data.database.AppDatabase
import com.kennzeichen.app.data.database.KennzeichenEntity
import com.kennzeichen.app.data.database.PlayerEntity
import com.kennzeichen.app.data.repository.DataLoader
import com.kennzeichen.app.data.repository.KennzeichenRepository
import com.kennzeichen.app.data.repository.PlayerRepository
import com.kennzeichen.app.data.repository.ScannedRepository
import com.kennzeichen.app.ui.screen.KennzeichenInputScreen
import com.kennzeichen.app.ui.screen.PlayerSelectionScreen
import com.kennzeichen.app.ui.screen.PlayerSetupScreen
import com.kennzeichen.app.ui.screen.ScanResultDialog
import com.kennzeichen.app.ui.screen.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database: AppDatabase by lazy { (application as KennzeichenApplication).getDatabase() }
    private val viewModel: MainViewModel by viewModels {
        MainViewModel(database)
    }

    private var _showSetup = mutableStateOf(false)
    private var _showKeyboard = mutableStateOf(false)
    private var _scanResult = mutableStateOf<MainViewModel.ScanResult?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Material3 {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    KennzeichenAppScreen(
                        viewModel = viewModel,
                        showSetup = _showSetup,
                        showKeyboard = _showKeyboard,
                        scanResult = _scanResult,
                        onSetupComplete = { names ->
                            lifecycleScope.launch {
                                names.forEach { name ->
                                    viewModel.addPlayer(name)
                                }
                            }
                            _showSetup.value = false
                        },
                        onPlayerSelect = { playerId ->
                            viewModel.selectPlayer(playerId)
                            _showKeyboard.value = true
                        },
                        onAddPlayer = { _showSetup.value = true },
                        onDeletePlayer = { playerId ->
                            viewModel.deletePlayer(playerId)
                        },
                        onKennzeichenInput = { text ->
                            viewModel.inputText = text
                        },
                        onSubmit = {
                            viewModel.onKennzeichenInput(viewModel.inputText)
                            _scanResult.value = viewModel.scanResult.value
                        },
                        onBackFromKeyboard = { _showKeyboard.value = false },
                        onDismissResult = {
                            _scanResult.value = null
                            viewModel.dismissScanResult()
                        },
                        onWikipedia = { /* TODO */ }
                    )
                }
            }
        }
        loadInitialData()
    }

    private fun loadInitialData() {
        lifecycleScope.launch {
            val count = database.kennzeichenDao().getCount().first()
            if (count == 0) {
                val loader = DataLoader(this@MainActivity)
                val kennzeichenList = loader.loadKennzeichenData()
                database.kennzeichenDao().insertAll(kennzeichenList)
            }
        }
    }
}

@Composable
fun KennzeichenAppScreen(
    viewModel: MainViewModel,
    showSetup: androidx.compose.runtime.MutableState<Boolean>,
    showKeyboard: androidx.compose.runtime.MutableState<Boolean>,
    scanResult: androidx.compose.runtime.MutableState<MainViewModel.ScanResult?>,
    onSetupComplete: (List<String>) -> Unit,
    onPlayerSelect: (Long) -> Unit,
    onAddPlayer: () -> Unit,
    onDeletePlayer: (Long) -> Unit,
    onKennzeichenInput: (String) -> Unit,
    onSubmit: () -> Unit,
    onBackFromKeyboard: () -> Unit,
    onDismissResult: () -> Unit,
    onWikipedia: () -> Unit
) {
    val players by viewModel.players.collectAsStateWithLifecycle()
    val selectedPlayerId = viewModel.selectedPlayerId

    if (showSetup.value || players.isEmpty()) {
        PlayerSetupScreen(onStartGame = onSetupComplete)
    } else if (showKeyboard.value) {
        val selectedPlayer = players.find { it.id == selectedPlayerId }
        KennzeichenInputScreen(
            selectedPlayerName = selectedPlayer?.name ?: "",
            inputText = viewModel.inputText,
            onTextChange = onKennzeichenInput,
            onSubmit = onSubmit,
            onBack = onBackFromKeyboard
        )
    } else {
        PlayerSelectionScreen(
            players = players,
            selectedPlayerId = selectedPlayerId,
            onPlayerClick = onPlayerSelect,
            onAddPlayer = onAddPlayer,
            onDeletePlayer = onDeletePlayer
        )
    }

    scanResult.value?.let { result ->
        ScanResultDialog(
            result = result,
            onDismiss = onDismissResult,
            onWikipedia = onWikipedia
        )
    }
}