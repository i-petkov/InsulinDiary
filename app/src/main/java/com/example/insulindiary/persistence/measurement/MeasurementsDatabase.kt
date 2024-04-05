package com.example.insulindiary.persistence.measurement

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.insulindiary.data.Measurement
import com.example.insulindiary.persistence.Converters

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
