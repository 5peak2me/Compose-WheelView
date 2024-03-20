package com.l3gacy.lib.compose.wheelview.picker.internal

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.min
import kotlin.time.DurationUnit

// <editor-fold desc="LocalDate" defaultstate="collapsed">
/**
 * Obtains the current date from the system clock in the default time-zone.
 * <p>
 * This will query the {@link Clock#systemDefaultZone() system clock} in the default
 * time-zone to obtain the current date.
 * <p>
 * Using this method will prevent the ability to use an alternate clock for testing
 * because the clock is hard-coded.
 *
 * @return the current date using the system clock and default time-zone, not null
 */
internal fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Clock.System.now().toLocalDateTime(timeZone).date
}

/**
 * The epoch year {@code LocalDate}, '1970-01-01'.
 */
internal val LocalDate.Companion.EPOCH: LocalDate
    get() = LocalDate(1970, 1, 1)

/**
 * The maximum supported [LocalDate], '2099-12-31'.
 * This could be used by an application as a "far future" date.
 */
internal val LocalDate.Companion.MAX: LocalDate
    get() = LocalDate(2099, 12, 31)

/**
 * Returns a copy of this {@code LocalDate} with the year altered.
 * <p>
 * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
 * <p>
 * This instance is immutable and unaffected by this method call.
 *
 * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
 * @return a [LocalDate] based on this date with the requested year, not null
 */
internal fun LocalDate.withYear(year: Int): LocalDate {
    return if (this.year == year) {
        this
    } else {
        resolvePreviousValid(year, monthNumber, dayOfMonth)
    }
}

/**
 * Returns a copy of this {@code LocalDate} with the month-of-year altered.
 * <p>
 * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
 * <p>
 * This instance is immutable and unaffected by this method call.
 *
 * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
 * @return a [LocalDate] based on this date with the requested month, not null
 */
internal fun LocalDate.withMonth(month: Int): LocalDate {
    return if (this.monthNumber == month) {
        this
    } else {
        resolvePreviousValid(year, month, dayOfMonth)
    }
}

/**
 * Returns a copy of this {@code LocalDate} with the day-of-month altered.
 * <p>
 * If the resulting date is invalid, an exception is thrown.
 * <p>
 * This instance is immutable and unaffected by this method call.
 *
 * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
 * @return a [LocalDate] based on this date with the requested day, not null
 */
internal fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
    return if (this.dayOfMonth == dayOfMonth) {
        this
    } else {
        LocalDate(year, monthNumber, dayOfMonth)
    }
}

/**
 * Resolves the date, resolving days past the end of month.
 *
 * @param year  the year to represent, validated from MIN_YEAR to MAX_YEAR
 * @param month  the month-of-year to represent, validated from 1 to 12
 * @param day  the day-of-month to represent, validated from 1 to 31
 * @return the resolved date, not null
 */
internal fun resolvePreviousValid(year: Int, month: Int, day: Int): LocalDate {
    val newDayOfMonth = when (month) {
        2 -> min(day, if (isLeapYear(year)) 29 else 28)
        4, 6, 9, 11 -> min(day, 30)
        else -> day
    }
    return LocalDate(year, month, newDayOfMonth)
}

/**
 * Checks if the year is a leap year, according to the ISO proleptic calendar system rules.
 */
internal fun isLeapYear(prolepticYear: Int): Boolean {
    return prolepticYear % 4 == 0 && (prolepticYear % 100 != 0 || prolepticYear % 400 == 0)
}

/**
 * Checks if this date is after the specified date.
 */
internal fun LocalDate.isBefore(other: LocalDate): Boolean = compareTo(other) < 0


/**
 * Checks if this date is before the specified date.
 */
internal fun LocalDate.isAfter(other: LocalDate): Boolean = compareTo(other) > 0
// </editor-fold>

// <editor-fold desc="LocalTime" defaultstate="collapsed">
internal fun LocalTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime {
    return Clock.System.now().toLocalDateTime(timeZone).time
}

internal val LocalTime.Companion.MIN: LocalTime
    get() = LocalTime(0, 0, 0, 0)
internal val LocalTime.Companion.MAX: LocalTime
    get() = LocalTime(23, 59, 59, 999_999_999)

internal fun LocalTime.withMinute(minute: Int): LocalTime {
    return if (this.minute == minute) {
        this
    } else {
        LocalTime(hour, minute, second, nanosecond)
    }
}

internal fun LocalTime.withHour(hour: Int): LocalTime {
    return if (this.hour == hour) {
        this
    } else {
        LocalTime(hour, minute, second, nanosecond)
    }
}

internal fun LocalTime.isBefore(other: LocalTime): Boolean = compareTo(other) < 0

internal fun LocalTime.isAfter(other: LocalTime): Boolean = compareTo(other) > 0
// </editor-fold>

// <editor-fold desc="LocalDateTime" defaultstate="collapsed">
internal fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timeZone)
}

internal val LocalDateTime.Companion.EPOCH: LocalDateTime
    get() = LocalDateTime(LocalDate.EPOCH, LocalTime.MIN)

internal val LocalDateTime.Companion.MAX: LocalDateTime
    get() = LocalDateTime(LocalDate.MAX, LocalTime.MAX)

internal fun LocalDateTime.with(date: LocalDate, time: LocalTime): LocalDateTime {
    return if (this.date == date && this.time == time) {
        this
    } else {
        LocalDateTime(date, time)
    }
}

internal fun LocalDateTime.withYear(year: Int): LocalDateTime {
    return with(date.withYear(year), time)
}

internal fun LocalDateTime.withMonthNumber(monthNumber: Int): LocalDateTime {
    return with(date.withMonth(monthNumber), time)
}

internal fun LocalDateTime.withDayOfMonth(dayOfMonth: Int): LocalDateTime {
    return with(date.withDayOfMonth(dayOfMonth), time)
}

internal fun LocalDateTime.withHour(hour: Int): LocalDateTime {
    return with(date, time.withHour(hour))
}

internal fun LocalDateTime.withMinute(minute: Int): LocalDateTime {
    return with(date, time.withMinute(minute))
}

internal fun LocalDateTime.isBefore(other: LocalDateTime): Boolean = compareTo(other) < 0

internal fun LocalDateTime.isAfter(other: LocalDateTime): Boolean = compareTo(other) > 0

internal fun LocalDateTime.truncatedTo(unit: DurationUnit): LocalDateTime {
    return when (unit) {
        DurationUnit.NANOSECONDS -> this

        DurationUnit.MICROSECONDS -> LocalDateTime(
            year,
            month,
            dayOfMonth,
            hour,
            minute,
            second,
            nanosecond / 1000
        )

        DurationUnit.MILLISECONDS -> LocalDateTime(
            year,
            month,
            dayOfMonth,
            hour,
            minute,
            second,
            nanosecond / 1000000
        )

        DurationUnit.SECONDS -> LocalDateTime(year, month, dayOfMonth, hour, minute, second)
        DurationUnit.MINUTES -> LocalDateTime(year, month, dayOfMonth, hour, minute)
        DurationUnit.HOURS -> LocalDateTime(year, month, dayOfMonth, hour, 0)
        DurationUnit.DAYS -> LocalDateTime(year, month, dayOfMonth, 0, 0)
        else -> throw IllegalArgumentException("The value `else` does not match any of the patterns.")
    }
}
// </editor-fold>
