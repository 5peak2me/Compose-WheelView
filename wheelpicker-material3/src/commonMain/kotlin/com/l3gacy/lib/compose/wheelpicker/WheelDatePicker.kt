package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelpicker.internal.MIN
import com.l3gacy.lib.compose.wheelpicker.internal.MAX
import com.l3gacy.lib.compose.wheelpicker.internal.DateTime
import com.l3gacy.lib.compose.wheelpicker.internal.capitalize
import com.l3gacy.lib.compose.wheelpicker.internal.lengthOfMonth
import com.l3gacy.lib.compose.wheelpicker.internal.now
import com.l3gacy.lib.compose.wheelpicker.internal.withDayOfMonth
import com.l3gacy.lib.compose.wheelpicker.internal.withMonth
import com.l3gacy.lib.compose.wheelpicker.internal.withYear
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/**
 *
 * Created by J!nl!n on 2023/12/25.
 *
 * Copyright © 2023 J!nl!n™ Inc. All Rights Reserved.
 *
 */
@Composable
fun WheelDatePicker(
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    endless: Boolean = true,
    onSelectedDate: (LocalDate) -> Unit,
) {
    var snappedDate by remember { mutableStateOf(initialDate) }

    val dayOfMonths = snappedDate.calculateDayOfMonth(minDate, maxDate)

    val months = snappedDate.calculateMonthOfYear(minDate, maxDate)

    val years = (minDate.year..maxDate.year).mapIndexed { index, item ->
        DateTime(
            text = item.toString(),
            value = item,
            index = index
        )
    }

    LaunchedEffect(snappedDate) {
        onSelectedDate(snappedDate)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            getDateFormatOrder().forEach { char ->
                when (char) {
                    'd' -> {
                        // DayOfMonth
                        key(dayOfMonths) {
                            WheelTextPicker(
                                modifier = Modifier.weight(1F),
                                endless = endless,
                                texts = dayOfMonths.map { it.text },
                                initialIndex = dayOfMonths.find { it.value == snappedDate.dayOfMonth }?.index ?: 0
                            ) { index ->
                                snappedDate = snappedDate.withDayOfMonth(dayOfMonths[index].value)
                            }
                        }
                    }
                    'M' -> {
                        // Month
                        key(months) {
                            WheelTextPicker(
                                modifier = Modifier.weight(1F),
                                endless = endless,
                                texts = months.map { it.text },
                                initialIndex = months.find { it.value == snappedDate.monthNumber }?.index ?: 0
                            ) { index ->
                                snappedDate = snappedDate.withMonth(months[index].value)
                            }
                        }
                    }
                    'y' -> {
                        // Year
                        WheelTextPicker(
                            modifier = Modifier.weight(1F),
                            endless = endless,
                            texts = years.map { it.text },
                            initialIndex = years.find { it.value == snappedDate.year }?.index ?: 0
                        ) { index ->
                            snappedDate = snappedDate.withYear(years[index].value)
                        }
                    }
                }
            }
        }
    }
}

private fun LocalDate.calculateDayOfMonth(minDate: LocalDate, maxDate: LocalDate): List<DateTime> {
    val range = when {
        year == minDate.year && month == minDate.month -> minDate.dayOfMonth..lengthOfMonth()
        year == maxDate.year && month == maxDate.month -> 1..maxDate.dayOfMonth
        else -> 1..lengthOfMonth()
    }
    return range.mapIndexed { index, item ->
        DateTime(
            text = item.toString(),
            value = item,
            index = index
        )
    }
}

private fun LocalDate.calculateMonthOfYear(minDate: LocalDate, maxDate: LocalDate): List<DateTime> {
    val range = when (year) {
        minDate.year -> minDate.monthNumber..12
        maxDate.year -> 1..maxDate.monthNumber
        else -> 1..12
    }
    return range.mapIndexed { index, item ->
        DateTime(
            text = Month(item).name.lowercase().capitalize,
            value = item,
            index = index
        )
    }
}
