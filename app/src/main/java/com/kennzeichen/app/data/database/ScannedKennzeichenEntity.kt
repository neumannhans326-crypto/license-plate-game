package com.kennzeichen.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "scanned_kennzeichen")
data class ScannedKennzeichenEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kennzeichen: String,
    val playerId: Long,
    val punkte: Int,
    val timestamp: LocalDateTime
)