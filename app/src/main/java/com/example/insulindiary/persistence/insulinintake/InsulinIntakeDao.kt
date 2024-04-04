package com.example.insulindiary.persistence.insulinintake

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.insulindiary.data.BaseInsulinIntake
import com.example.insulindiary.data.DailyInsulinIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface InsulinIntakeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dailyInsulinIntake: DailyInsulinIntake)

    @Update
    suspend fun update(dailyInsulinIntake: DailyInsulinIntake)

    @Delete
    suspend fun delete(dailyInsulinIntake: DailyInsulinIntake)

    @Query("SELECT * from DailyInsulinIntakes")
    fun getAllDailyIntakes(): Flow<List<DailyInsulinIntake>>

    @Query("SELECT * from DailyInsulinIntakes WHERE date BETWEEN :startDate and :endDate ORDER BY date ASC")
    fun getAllDailyIntakesBetween(
        startDate: Long,
        endDate: Long
    ): Flow<List<DailyInsulinIntake>>

    @Query("SELECT * from DailyInsulinIntakes WHERE date = :date ORDER BY date ASC")
    fun getAllDailyIntakesAt(date: Long): Flow<List<DailyInsulinIntake>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(baseInsulinIntake: BaseInsulinIntake)

    @Update
    suspend fun update(baseInsulinIntake: BaseInsulinIntake)

    @Delete
    suspend fun delete(baseInsulinIntake: BaseInsulinIntake)

    @Query("SELECT * from BaseInsulinIntakes")
    fun getAllBaseIntakes(): Flow<List<BaseInsulinIntake>>

    @Query("SELECT * from BaseInsulinIntakes WHERE id = :id")
    fun getBaseIntake(id: Long): Flow<BaseInsulinIntake>
}
