package com.example.insulindiary.persistence.insulinintake

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.insulindiary.data.DailyInsulinIntake
import com.example.insulindiary.data.InsulinIntakePlan
import com.example.insulindiary.persistence.Converters

@Database(entities = [DailyInsulinIntake::class, InsulinIntakePlan::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InsulinIntakeDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: InsulinIntakeDatabase? = null

        fun getDatabase(context: Context): InsulinIntakeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, InsulinIntakeDatabase::class.java, "insulin_intake_database")
                    .build()
                    .also { instance = it }
            }
        }
    }

    abstract fun insulinIntakeDao(): InsulinIntakeDao
}
