package com.kennzeichen.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ScannedKennzeichenDao {

    @Query("SELECT * FROM scanned_kennzeichen ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ScannedKennzeichenEntity>>

    @Query("SELECT * FROM scanned_kennzeichen WHERE kennzeichen = :kennzeichen")
    fun getByKennzeichen(kennzeichen: String): Flow<ScannedKennzeichenEntity?>

    @Query("SELECT * FROM scanned_kennzeichen WHERE kennzeichen = :kennzeichen")
    fun getByKennzeichenSync(kennzeichen: String): ScannedKennzeichenEntity?

    @Query("SELECT * FROM scanned_kennzeichen WHERE playerId = :playerId ORDER BY timestamp DESC")
    fun getByPlayer(playerId: Long): Flow<List<ScannedKennzeichenEntity>>

    @Query("SELECT SUM(punkte) FROM scanned_kennzeichen")
    fun getTotalPoints(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM scanned_kennzeichen")
    fun getScannedCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ScannedKennzeichenEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ScannedKennzeichenEntity>)

    @Query("DELETE FROM scanned_kennzeichen")
    suspend fun deleteAll()
}