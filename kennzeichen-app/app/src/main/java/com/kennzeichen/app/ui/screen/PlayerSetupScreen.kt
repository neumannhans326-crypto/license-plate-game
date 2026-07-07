package com.kennzeichen.app.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kennzeichen.app.ui.viewmodel.PlayerSetupViewModel
import com.kennzeichen.app.ui.component.PrimaryButton
import com.kennzeichen.app.ui.component.PlayerNameInput
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSetupScreen(
    onStartGame: (List<String>) -> Unit
) {
    val viewModel: PlayerSetupViewModel = viewModel()
    val playerNames by viewModel.playerNames.collectAsStateWithLifecycle()
    val newPlayerName by remember { mutableStateOf("") }
    val focusedField by remember { mutableStateOf(-1) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kennzeichen-Spiel",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Namen der Mitspieler eingeben",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            playerNames.forEachIndexed { index, name ->
                PlayerNameInput(
                    name = name,
                    index = index,
                    isFocused = focusedField == index,
                    onNameChange = { newName ->
                        viewModel.updatePlayerName(index, newName)
                    },
                    onRemove = {
                        if (playerNames.size > 1) {
                            viewModel.removePlayer(index)
                        }
                    },
                    onFocusChange = { hasFocus ->
                        focusedField = if (hasFocus) index else -1
                    },
                    onNext = {
                        if (index == playerNames.size - 1) {
                            focusManager.clearFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                )
            }
        }

        if (playerNames.size < 6) {
            PrimaryButton(
                text = "Spieler hinzufügen",
                onClick = { viewModel.addPlayer() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Spiel starten (${playerNames.size} Spieler)",
            onClick = { onStartGame(playerNames.filter { it.isNotBlank() }) },
            modifier = Modifier.fillMaxWidth(),
            enabled = playerNames.any { it.isNotBlank() }
        )
    }
}