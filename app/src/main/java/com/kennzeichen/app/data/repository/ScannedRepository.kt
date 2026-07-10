package com.kennzeichen.app.data.repository

import com.kennzeichen.app.data.database.AppDatabase
import com.kennzeichen.app.data.database.ScannedKennzeichenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class ScannedRepository(private val database: AppDatabase) {

    private val dao = database.scannedKennzeichenDao()

    fun getAll(): Flow<List<ScannedKennzeichenEntity>> = dao.getAll()

    fun getByKennzeichen(kennzeichen: String): Flow<ScannedKennzeichenEntity?> = dao.getByKennzeichen(kennzeichen)

    fun getByPlayer(playerId: Long): Flow<List<ScannedKennzeichenEntity>> = dao.getByPlayer(playerId)

    suspend fun isScanned(kennzeichen: String): Boolean {
        return dao.getByKennzeichenSync(kennzeichen) != null
    }

    suspend fun addScanned(kennzeichen: String, playerId: Long, punkte: Int): Long {
        val entity = ScannedKennzeichenEntity(
            kennzeichen = kennzeichen,
            playerId = playerId,
            punkte = punkte,
            timestamp = LocalDateTime.now()
        )
        return dao.insert(entity)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun getTotalPoints(): Flow<Int> = dao.getTotalPoints().map { it ?: 0 }

    fun getScannedCount(): Flow<Int> = dao.getScannedCount()
}