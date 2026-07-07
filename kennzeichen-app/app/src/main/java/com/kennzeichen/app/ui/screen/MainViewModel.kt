package com.kennzeichen.app.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kennzeichen.app.data.database.AppDatabase
import com.kennzeichen.app.data.database.KennzeichenEntity
import com.kennzeichen.app.data.database.PlayerEntity
import com.kennzeichen.app.data.database.ScannedKennzeichenEntity
import com.kennzeichen.app.data.repository.KennzeichenRepository
import com.kennzeichen.app.data.repository.PlayerRepository
import com.kennzeichen.app.data.repository.ScannedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(private val database: AppDatabase) : ViewModel() {

    private val kennzeichenRepository = KennzeichenRepository(database)
    private val playerRepository = PlayerRepository(database)
    private val scannedRepository = ScannedRepository(database)

    private val _players = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val players = _players

    private val _selectedPlayerId = MutableStateFlow<Long?>(null)
    var selectedPlayerId: Long?
        get() = _selectedPlayerId.value
        set(value) { _selectedPlayerId.value = value }

    private val _inputText = mutableStateOf("")
    var inputText: String
        get() = _inputText.value
        set(value) { _inputText.value = value }

    private val _scanResult = MutableStateFlow<ScanResult?>(null)
    val scanResult = _scanResult

    private val _showWikipediaDialog = MutableStateFlow<Boolean>(false)
    val showWikipediaDialog = _showWikipediaDialog

    private val _lastScannedKennzeichen = MutableStateFlow<String?>(null)
    var lastScannedKennzeichen: String?
        get() = _lastScannedKennzeichen.value
        set(value) { _lastScannedKennzeichen.value = value }

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            playerRepository.getAll().collect { playerList ->
                _players.value = playerList
                if (_selectedPlayerId.value == null && playerList.isNotEmpty()) {
                    _selectedPlayerId.value = playerList.first().id
                }
            }
        }
    }

    fun addPlayer(name: String) {
        viewModelScope.launch {
            val id = playerRepository.createPlayer(name.trim())
            _selectedPlayerId.value = id
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            playerRepository.deletePlayer(id)
        }
    }

    fun selectPlayer(id: Long) {
        _selectedPlayerId.value = id
    }

    fun onKennzeichenInput(kennzeichen: String) {
        val cleaned = kennzeichen.trim().uppercase()
        if (cleaned.isEmpty()) return

        val selectedId = _selectedPlayerId.value ?: return

        viewModelScope.launch {
            val existingScan = scannedRepository.isScanned(cleaned)
            if (existingScan) {
                val scannedEntity = database.scannedKennzeichenDao().getByKennzeichenSync(cleaned)
                val playerName = database.playerDao().getByIdSync(scannedEntity?.playerId ?: 0)?.name ?: "Jemand"
                val timeStr = scannedEntity?.timestamp?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "unbekannt"
                _scanResult.value = ScanResult.AlreadyScanned(playerName, timeStr, cleaned)
                return@launch
            }

            val kennzeichenEntity = database.kennzeichenDao().getByKuerzelSync(cleaned)
            if (kennzeichenEntity == null) {
                _scanResult.value = ScanResult.NotFound(cleaned)
                return@launch
            }

            val punkte = kennzeichenRepository.calculatePunkte(kennzeichenEntity)
            val info = kennzeichenRepository.getKennzeichenInfo(kennzeichenEntity)

            scannedRepository.addScanned(cleaned, selectedId, punkte)
            kennzeichenRepository.markAsFound(cleaned, System.currentTimeMillis(), "Auto", "")
            playerRepository.addPoints(selectedId, punkte)

            _lastScannedKennzeichen.value = cleaned
            _scanResult.value = ScanResult.Success(kennzeichenEntity, punkte, info)
            _showWikipediaDialog.value = true
        }
    }

    fun dismissScanResult() {
        _scanResult.value = null
        _showWikipediaDialog.value = false
        inputText = ""
    }

    fun openWikipedia(kennzeichen: KennzeichenEntity) {
        val url = "https://de.wikipedia.org/wiki/${kennzeichen.stadt}"
        // TODO: Open in browser
    }

    data class ScanResult(
        val type: ScanResultType,
        val kennzeichenEntity: KennzeichenEntity? = null,
        val punkte: Int = 0,
        val info: String = "",
        val playerName: String = "",
        val time: String = "",
        val kennzeichen: String = ""
    ) {
        sealed class ScanResultType {
            object Success : ScanResultType()
            object AlreadyScanned : ScanResultType()
            object NotFound : ScanResultType()
        }

        companion object {
            fun Success(kennzeichenEntity: KennzeichenEntity, punkte: Int, info: String) = 
                ScanResult(ScanResultType.Success(), kennzeichenEntity, punkte, info)
            fun AlreadyScanned(playerName: String, time: String, kennzeichen: String) = 
                ScanResult(ScanResultType.AlreadyScanned(), playerName = playerName, time = time, kennzeichen = kennzeichen)
            fun NotFound(kennzeichen: String) = 
                ScanResult(ScanResultType.NotFound(), kennzeichen = kennzeichen)
        }
    }
}