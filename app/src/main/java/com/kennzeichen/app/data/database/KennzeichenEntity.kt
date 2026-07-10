package com.kennzeichen.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kennzeichen")
data class KennzeichenEntity(
    @PrimaryKey val kuerzel: String,
    val stadt: String,
    val bundesland: String,
    val typ: String,
    val bevoelkerung: Int? = null,
    val flaeche: Double? = null,
    val kennzeichenStart: String? = null,
    val kennzeichenEnde: String? = null,
    val istStadt: Boolean = false,
    val istLandkreis: Boolean = false,
    val istRegion: Boolean = false,
    val istKreis: Boolean = false,
    val istRegionKreis: Boolean = false,
    val gesamt: Int? = null,
    val kfz: Int? = null,
    val kraftrad: Int? = null,
    val lastkraftwagen: Int? = null,
    val zugmaschine: Int? = null,
    val anhanger: Int? = null,
    val sonstige: Int? = null,
    var istGefunden: Boolean = false,
    var gefundenAm: Long? = null,
    var fundort: String? = null,
    var notiz: String? = null
)