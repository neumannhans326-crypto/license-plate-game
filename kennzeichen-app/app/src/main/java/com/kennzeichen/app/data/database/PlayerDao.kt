package com.kennzeichen.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.List

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE aktiv = 1 ORDER BY erstelltAm ASC")
    fun getAll(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getById(id: Long): Flow<PlayerEntity?>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getByIdSync(id: Long): PlayerEntity?

    @Query("SELECT * FROM players WHERE name = :name")
    fun getByName(name: String): Flow<PlayerEntity?>

    @Query("SELECT COUNT(*) FROM players WHERE aktiv = 1")
    fun getActiveCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: PlayerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Update
    suspend fun update(player: PlayerEntity)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM players")
    suspend fun deleteAll()

    @Query("UPDATE players SET punktzahl = punktzahl + :punkte WHERE id = :id")
    suspend fun addPoints(id: Long, punkte: Int)

    @Query("UPDATE players SET punktzahl = :punkte WHERE id = :id")
    suspend fun setPoints(id: Long, punkte: Int)
}