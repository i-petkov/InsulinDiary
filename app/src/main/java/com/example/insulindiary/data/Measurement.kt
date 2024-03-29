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
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.time.ZonedDateTime

@Entity(tableName = "Measurements")
data class Measurement(@PrimaryKey val time: ZonedDateTime, val value: Double)

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

    @Query("SELECT * from Measurements WHERE time BETWEEN :start and :end")
    fun getAllItemsBetween(start: Long, end: Long): Flow<List<Measurement>>
}

@Database(entities = [Measurement::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MeasurementsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var Instance: MeasurementsDatabase? = null

        fun getDatabase(context: Context): MeasurementsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MeasurementsDatabase::class.java, "measurements_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
    abstract fun measurementDao(): MeasurementDao

}

class Converters {
    @TypeConverter
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime): Long =
        zonedDateTime.toUtc().toInstant().toEpochMilli()

    @TypeConverter
    fun toZonedDateTime(dateTime: Long):ZonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime), zoneIdUtc)
}