package com.kennzeichen.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kennzeichen.app.ui.component.KeyboardButton

@Composable
fun KennzeichenInputScreen(
    selectedPlayerName: String,
    inputText: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Spieler: $selectedPlayerName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            )
        }

        // Input display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inputText.ifEmpty { "— — —" },
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center,
                    color = if (inputText.isNotEmpty()) 
                        androidx.compose.material3.MaterialTheme.colorScheme.primary 
                    else 
                        androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Keyboard
        Keyboard(
            onKeyPress = { key ->
                when (key) {
                    "BACKSPACE" -> {
                        if (inputText.isNotEmpty()) {
                            onTextChange(inputText.dropLast(1))
                        }
                    }
                    "ENTER" -> onSubmit()
                    else -> {
                        if (inputText.length < 6 && key.matches(Regex("[A-ZÄÖÜ]"))) {
                            onTextChange(inputText + key)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        // Back button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .height(48.dp),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
        ) {
            Text(text = "Zurück zur Spielerauswahl", fontSize = 16.sp)
        }
    }
}

@Composable
fun Keyboard(
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = listOf(
        "Q W E R T Z U I O P".split(" "),
        "A S D F G H J K L".split(" "),
        "Ä Y X C V B N M Ö Ü".split(" "),
        listOf("BACKSPACE", "ENTER")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (rowIndex == 3) Arrangement.SpaceBetween else Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    val isWide = key == "BACKSPACE" || key == "ENTER"
                    KeyboardButton(
                        text = key,
                        onClick = { onKeyPress(key) },
                        modifier = Modifier
                            .weight(if (isWide) 2f else 1f, fill = false)
                            .height(56.dp),
                        isWide = isWide
                    )
                }
            }
        }
    }
}