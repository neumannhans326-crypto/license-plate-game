package com.kennzeichen.app.data.repository

import com.kennzeichen.app.data.database.AppDatabase
import com.kennzeichen.app.data.database.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val database: AppDatabase) {

    private val dao = database.playerDao()

    fun getAll(): Flow<List<PlayerEntity>> = dao.getAll()

    fun getById(id: Long): Flow<PlayerEntity?> = dao.getById(id)

    fun getByName(name: String): Flow<PlayerEntity?> = dao.getByName(name)

    fun getActiveCount(): Flow<Int> = dao.getActiveCount()

    suspend fun createPlayer(name: String): Long {
        val player = PlayerEntity(name = name)
        return dao.insert(player)
    }

    suspend fun updatePlayer(player: PlayerEntity) {
        dao.update(player)
    }

    suspend fun deletePlayer(id: Long) {
        dao.deleteById(id)
    }

    suspend fun addPoints(playerId: Long, points: Int) {
        dao.addPoints(playerId, points)
    }

    suspend fun setPoints(playerId: Long, points: Int) {
        dao.setPoints(playerId, points)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}