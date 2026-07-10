package com.kennzeichen.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kennzeichen.app.data.database.PlayerEntity
import com.kennzeichen.app.ui.component.PrimaryButton

@Composable
fun PlayerSelectionScreen(
    players: List<PlayerEntity>,
    selectedPlayerId: Long?,
    onPlayerClick: (Long) -> Unit,
    onAddPlayer: () -> Unit,
    onDeletePlayer: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kennzeichen-Spiel",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Wer spielt mit?",
            fontSize = 18.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            players.forEach { player ->
                val isSelected = player.id == selectedPlayerId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = if (isSelected) 
                            androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer 
                            else androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    onClick = { onPlayerClick(player.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Spieler",
                                tint = if (isSelected) 
                                    androidx.compose.material3.MaterialTheme.colorScheme.primary 
                                    else androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Column {
                                Text(
                                    text = player.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) 
                                        androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer 
                                        else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${player.punktzahl} Punkte",
                                    fontSize = 16.sp,
                                    color = if (isSelected) 
                                        androidx.compose.material3.MaterialTheme.colorScheme.primary 
                                        else androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (!isSelected) {
                            androidx.compose.material3.IconButton(
                                onClick = { onDeletePlayer(player.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Löschen",
                                    tint = androidx.compose.material3.MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        PrimaryButton(
            text = "Spieler hinzufügen",
            onClick = onAddPlayer,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
                contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
    }
}