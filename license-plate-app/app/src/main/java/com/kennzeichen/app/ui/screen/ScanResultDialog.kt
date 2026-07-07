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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Wikipedia
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kennzeichen.app.data.database.KennzeichenEntity
import com.kennzeichen.app.ui.component.PrimaryButton

@Composable
fun ScanResultDialog(
    result: MainViewModel.ScanResult,
    onDismiss: () -> Unit,
    onWikipedia: () -> Unit
) {
    val isSuccess = result.type is MainViewModel.ScanResult.ScanResultType.Success
    val isAlreadyScanned = result.type is MainViewModel.ScanResult.ScanResultType.AlreadyScanned
    val isNotFound = result.type is MainViewModel.ScanResult.ScanResultType.NotFound

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = when {
                    isSuccess -> "Kennzeichen erfasst! 🎉"
                    isAlreadyScanned -> "Bereits erfasst"
                    isNotFound -> "Nicht gefunden"
                    else -> "Ergebnis"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isSuccess -> androidx.compose.material3.MaterialTheme.colorScheme.primary
                    isAlreadyScanned -> androidx.compose.material3.MaterialTheme.colorScheme.warning
                    isNotFound -> androidx.compose.material3.MaterialTheme.colorScheme.error
                    else -> androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                }
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isSuccess) {
                    val kennzeichen = result.kennzeichenEntity!!
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = kennzeichen.kuerzel,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "+${result.punkte} Punkte",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = result.info,
                                fontSize = 16.sp,
                                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                } else if (isAlreadyScanned) {
                    Text(
                        text = "\"${result.kennzeichen}\" wurde von ${result.playerName} um ${result.time} bereits erfasst.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                } else if (isNotFound) {
                    Text(
                        text = "Das Kennzeichen \"${result.kennzeichen}\" ist nicht in der Datenbank.\nPrüfe die Schreibweise oder es ist ein sehr neues Kennzeichen.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            if (isSuccess) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onWikipedia()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f, fill = true)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wikipedia,
                                contentDescription = "Wikipedia",
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.foundation.layout.Modifier.width(8.dp))
                            Text(text = "Wikipedia", fontSize = 16.sp)
                        }
                    }
                    PrimaryButton(
                        text = "OK",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f, fill = true)
                    )
                }
            } else {
                PrimaryButton(
                    text = "OK",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}