package com.example.insulindiary.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "Measurements")
data class Measurement(
    @PrimaryKey val date: LocalDate,
    val time: LocalTime,
    val value: Double
)
