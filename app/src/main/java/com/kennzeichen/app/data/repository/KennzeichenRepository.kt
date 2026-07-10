package com.kennzeichen.app.data.repository

import com.kennzeichen.app.data.database.KennzeichenDao
import com.kennzeichen.app.data.database.KennzeichenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KennzeichenRepository(private val dao: KennzeichenDao) {

    fun getAll(): Flow<List<KennzeichenEntity>> = dao.getAll().map { it as List<KennzeichenEntity> }

    fun getByKuerzel(kuerzel: String): Flow<KennzeichenEntity?> = dao.getByKuerzel(kuerzel).map { it as KennzeichenEntity? }

    fun search(query: String): Flow<List<KennzeichenEntity>> = dao.search(query).map { it as List<KennzeichenEntity> }

    fun getGefunden(): Flow<List<KennzeichenEntity>> = dao.getGefunden().map { it as List<KennzeichenEntity> }

    fun getNichtGefunden(): Flow<List<KennzeichenEntity>> = dao.getNichtGefunden().map { it as List<KennzeichenEntity> }

    fun getByBundesland(bundesland: String): Flow<List<KennzeichenEntity>> = dao.getByBundesland(bundesland).map { it as List<KennzeichenEntity> }

    fun getCount(): Flow<Int> = dao.getCount()

    fun getGefundenCount(): Flow<Int> = dao.getGefundenCount()

    fun getAllBundeslaender(): Flow<List<String>> = dao.getAllBundeslaender().map { it as List<String> }

    suspend fun markAsFound(kuerzel: String, zeit: Long, ort: String?, notiz: String?) {
        dao.markAsFound(kuerzel, zeit, ort, notiz)
    }

    suspend fun markAsNotFound(kuerzel: String) {
        dao.markAsNotFound(kuerzel)
    }

    fun calculatePunkte(kennzeichen: KennzeichenEntity): Int {
        if (kennzeichen.kuerzel == "BÜS" || kennzeichen.kuerzel == "BUS") {
            return 100
        }

        val kfz = kennzeichen.kfz ?: kennzeichen.gesamt ?: 0

        return when {
            kfz <= 10000 -> 10
            kfz <= 50000 -> 8
            kfz <= 100000 -> 6
            kfz <= 200000 -> 4
            kfz <= 500000 -> 2
            else -> 1
        }
    }

    fun getKennzeichenInfo(kennzeichen: KennzeichenEntity): String {
        val stadt = kennzeichen.stadt
        val kfz = kennzeichen.kfz ?: kennzeichen.gesamt ?: 0
        return "$stadt ist das Kennzeichen der Stadt $stadt, es gibt ${formatNumber(kfz)} Fahrzeuge mit diesem Kennzeichen."
    }

    private fun formatNumber(number: Int): String {
        return if (number >= 1000000) {
            "${number / 1000000}M"
        } else if (number >= 1000) {
            "${number / 1000}K"
        } else {
            number.toString()
        }
    }
}
