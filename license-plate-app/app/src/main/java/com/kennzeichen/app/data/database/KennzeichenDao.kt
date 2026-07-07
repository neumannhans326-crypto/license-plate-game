package com.kennzeichen.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.List

@Dao
interface KennzeichenDao {

    @Query("SELECT * FROM kennzeichen ORDER BY kuerzel ASC")
    fun getAll(): Flow<List<KennzeichenEntity>>

    @Query("SELECT * FROM kennzeichen WHERE kuerzel = :kuerzel")
    fun getByKuerzel(kuerzel: String): Flow<KennzeichenEntity?>

    @Query("SELECT * FROM kennzeichen WHERE kuerzel = :kuerzel")
    fun getByKuerzelSync(kuerzel: String): KennzeichenEntity?

    @Query("SELECT * FROM kennzeichen WHERE kuerzel LIKE :query OR stadt LIKE :query OR bundesland LIKE :query ORDER BY kuerzel ASC")
    fun search(query: String): Flow<List<KennzeichenEntity>>

    @Query("SELECT * FROM kennzeichen WHERE istGefunden = 1 ORDER BY gefundenAm DESC")
    fun getGefunden(): Flow<List<KennzeichenEntity>>

    @Query("SELECT * FROM kennzeichen WHERE istGefunden = 0 ORDER BY kuerzel ASC")
    fun getNichtGefunden(): Flow<List<KennzeichenEntity>>

    @Query("SELECT * FROM kennzeichen WHERE bundesland = :bundesland ORDER BY kuerzel ASC")
    fun getByBundesland(bundesland: String): Flow<List<KennzeichenEntity>>

    @Query("SELECT COUNT(*) FROM kennzeichen")
    fun getCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM kennzeichen WHERE istGefunden = 1")
    fun getGefundenCount(): Flow<Int>

    @Query("SELECT DISTINCT bundesland FROM kennzeichen ORDER BY bundesland ASC")
    fun getAllBundeslaender(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kennzeichen: List<KennzeichenEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kennzeichen: KennzeichenEntity)

    @Update
    suspend fun update(kennzeichen: KennzeichenEntity)

    @Query("UPDATE kennzeichen SET istGefunden = 1, gefundenAm = :zeit, fundort = :ort, notiz = :notiz WHERE kuerzel = :kuerzel")
    suspend fun markAsFound(kuerzel: String, zeit: Long, ort: String?, notiz: String?)

    @Query("UPDATE kennzeichen SET istGefunden = 0, gefundenAm = NULL, fundort = NULL, notiz = NULL WHERE kuerzel = :kuerzel")
    suspend fun markAsNotFound(kuerzel: String)

    @Query("DELETE FROM kennzeichen")
    suspend fun deleteAll()
}