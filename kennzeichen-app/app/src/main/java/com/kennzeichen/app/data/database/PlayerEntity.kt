package com.kennzeichen.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "players")
@Serializable
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    var punktzahl: Int = 0,
    val erstelltAm: Long = System.currentTimeMillis(),
    var aktiv: Boolean = true
)