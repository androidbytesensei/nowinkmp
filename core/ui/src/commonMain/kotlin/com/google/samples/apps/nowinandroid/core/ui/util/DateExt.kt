/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.ui.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Helper extension function to get a properly formatted month name
 */
fun Month.getDisplayName(): String {
    // Convert the enum name to a properly capitalized month name
    return when (this) {
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        Month.DECEMBER -> "December"
        else -> "Unknown"
    }
}

/**
 * Formats an Instant according to the specified pattern
 *
 * @param publishDate The instant to format
 * @param pattern The pattern to use (e.g., "MEDIUM" for medium length, "SHORT" for short format)
 * @return A formatted date string
 */
@Composable
fun dateFormatted(publishDate: Instant, pattern: DateFormatPattern): String {
    val localDateTime = publishDate.toLocalDateTime(TimeZone.currentSystemDefault())

    return when (pattern) {
        DateFormatPattern.FULL -> {
            val monthName = localDateTime.month.getDisplayName()
            val dayOfWeek = getDayOfWeekName(localDateTime)
            "$dayOfWeek, $monthName ${localDateTime.dayOfMonth}, ${localDateTime.year}"
        }

        DateFormatPattern.LONG -> {
            val monthName = localDateTime.month.getDisplayName()
            "$monthName ${localDateTime.dayOfMonth}, ${localDateTime.year}"
        }

        DateFormatPattern.MEDIUM -> {
            val monthName = localDateTime.month.getDisplayName().take(3)
            "$monthName ${localDateTime.dayOfMonth}, ${localDateTime.year}"
        }

        DateFormatPattern.SHORT -> {
            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth}/${localDateTime.year}"
        }

        DateFormatPattern.ISO -> {
            "${localDateTime.year}-${
                localDateTime.monthNumber.toString().padStart(2, '0')
            }-${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
        }
    }
}

/**
 * Gets the day of week name for the given LocalDateTime
 * (A simple implementation since kotlinx.datetime doesn't provide direct day of week name)
 */
private fun getDayOfWeekName(dateTime: LocalDateTime): String {
    // This is a simplified calculation to get day of week
    // Note: This may not be accurate for all historical dates due to calendar changes
    val y = dateTime.year
    val m = dateTime.monthNumber
    val d = dateTime.dayOfMonth

    // Zeller's Congruence algorithm to calculate day of week
    val adjustedMonth = if (m < 3) m + 12 else m
    val adjustedYear = if (m < 3) y - 1 else y

    val h =
        (d + (13 * (adjustedMonth + 1)) / 5 + adjustedYear + adjustedYear / 4 - adjustedYear / 100 + adjustedYear / 400) % 7

    return when (h) {
        0 -> "Saturday"
        1 -> "Sunday"
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        else -> "Unknown"
    }
}

/**
 * Enum for date format patterns
 */
enum class DateFormatPattern {
    FULL,   // Wednesday, January 15, 2023
    LONG,   // January 15, 2023
    MEDIUM, // Jan 15, 2023
    SHORT,  // 1/15/2023
    ISO     // 2023-01-15
}