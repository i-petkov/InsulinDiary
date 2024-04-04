package com.example.insulindiary.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "Measurements")
data class Measurement(
    @PrimaryKey val date: LocalDate,
    val time: LocalTime,
    val value: Double
)

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

@Database(entities = [Measurement::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MeasurementsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: MeasurementsDatabase? = null

        fun getDatabase(context: Context): MeasurementsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, MeasurementsDatabase::class.java, "measurements_database")
                    .build()
                    .also { instance = it }
            }
        }
    }

    abstract fun measurementDao(): MeasurementDao
}

class Converters {
    @TypeConverter
    fun fromLocalTime(localDate: LocalTime): Int = localDate.toSecondOfDay()

    @TypeConverter
    fun toLocalTime(secondOfDay: Int): LocalTime = LocalTime.ofSecondOfDay(secondOfDay.toLong())

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): Long = localDate.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)
}
