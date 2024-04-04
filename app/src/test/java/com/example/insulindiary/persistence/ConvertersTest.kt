package com.example.insulindiary.persistence

import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ConvertersTest {

    companion object {
        val timeTestData = mapOf(
            56700 to LocalTime.of(15, 45),
            64320 to LocalTime.of(17, 52),
            76260 to LocalTime.of(21, 11),
            14340 to LocalTime.of(3, 59),
            86340 to LocalTime.of(23, 59)
        )

        val dateTestData = mapOf(
            19723L to LocalDate.of(2024, 1, 1),
            19817L to LocalDate.of(2024, 4, 4),
            20088L to LocalDate.of(2024, 12, 31),
            18628L to LocalDate.of(2021, 1, 1),
            18721L to LocalDate.of(2021, 4, 4),
            18992L to LocalDate.of(2021, 12, 31)
        )
    }

    private val converter = Converters()

    @Test
    fun fromLocalTime() {
        for (timeData in timeTestData) {
            val expected = timeData.key
            val actual = converter.fromLocalTime(timeData.value)
            assert(expected == actual)
        }
    }

    @Test
    fun toLocalTime() {
        for (timeData in timeTestData) {
            val expected = timeData.value
            val actual = converter.toLocalTime(timeData.key)
            assert(expected == actual)
        }
    }

    @Test
    fun fromLocalDate() {
        for (dateData in dateTestData) {
            val expected = dateData.key
            val actual = converter.fromLocalDate(dateData.value)
            assert(expected == actual)
        }
    }

    @Test
    fun toLocalDate() {
        for (dateData in dateTestData) {
            val expected = dateData.value
            val actual = converter.toLocalDate(dateData.key)
            assert(expected == actual)
        }
    }
}