package com.example.insulindiary.persistence.measurement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.insulindiary.data.Measurement
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(measurement: Measurement)

    @Update
    suspend fun update(measurement: Measurement)

    @Delete
    suspend fun delete(measurement: Measurement)

    @Query("SELECT * from Measurements")
    fun getAllItems(): Flow<List<Measurement>>

    @Query("SELECT * from Measurements WHERE date BETWEEN :startDate and :endDate ORDER BY date ASC")
    fun getAllItemsBetween(
        startDate: Long,
        endDate: Long
    ): Flow<List<Measurement>>

    @Query("SELECT * from Measurements WHERE date = :date ORDER BY date ASC")
    fun getAllItemsAt(date: Long): Flow<List<Measurement>>
}