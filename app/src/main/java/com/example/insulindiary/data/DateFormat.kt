package com.example.insulindiary.data

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/** Hours and Minutes in 24h format (21:30) */
val hh_mm = DateTimeFormatter.ofPattern("HH:mm")
/** Hours and Minutes in 12h format (09:30 PM) */
val hh_mm_a = DateTimeFormatter.ofPattern("hh:mm a")
/** month and year (March, 2024) */
val mmmm_yyyy = DateTimeFormatter.ofPattern("MMMM, yyyy")
/** month and year (02 March, 2024) */
val dd_mmmm_yyyy = DateTimeFormatter.ofPattern("dd MMMM, yyyy")

val zoneIdUtc = ZoneId.of("UTC")

fun currentTimeUtc() = ZonedDateTime.now(zoneIdUtc)

fun currentTimeLocal() = ZonedDateTime.now(ZoneId.systemDefault())

fun ZonedDateTime.toUtc() = withZoneSameInstant(zoneIdUtc)

fun ZonedDateTime.toLocal() = withZoneSameInstant(ZoneId.systemDefault())

fun ZonedDateTime.formatTime(zoneId: ZoneId = ZoneId.systemDefault()) = format(hh_mm.withZone(zoneId))

fun ZonedDateTime.formatMonthAndYear(zoneId: ZoneId = ZoneId.systemDefault()) = format(mmmm_yyyy.withZone(zoneId))

fun ZonedDateTime.formatDayMonthAndYear(zoneId: ZoneId = ZoneId.systemDefault()) = format(dd_mmmm_yyyy.withZone(zoneId))

fun ZonedDateTime.formatDateTime(zoneId: ZoneId = ZoneId.systemDefault()) = format(DateTimeFormatter.ISO_DATE_TIME.withZone(zoneId))

fun ZonedDateTime.formatDateTimeUtc() = formatDateTime(zoneIdUtc)

fun String.parseDateTime(): ZonedDateTime = ZonedDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
