package com.kennzeichen.app.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isWide: Boolean = false
) {
    val colors = if (text == "BACKSPACE" || text == "ENTER") {
        ButtonDefaults.buttonColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .padding(if (isWide) 8.dp else 4.dp),
        colors = colors,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = if (isWide) 16.sp else 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}