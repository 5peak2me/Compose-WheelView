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
import com.l3gacy.lib.compose.wheelpicker.internal.EPOCH
import com.l3gacy.lib.compose.wheelpicker.internal.MAX
import com.l3gacy.lib.compose.wheelpicker.internal.capitalize
import com.l3gacy.lib.compose.wheelpicker.internal.isLeapYear
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
    minDate: LocalDate = LocalDate.EPOCH,
    maxDate: LocalDate = LocalDate.MAX,
    dateFormat: DateFormat = DateFormat.DAY_MONTH_YEAR,
    endless: Boolean = false,
    onSelectedDate: (LocalDate) -> Unit,
) {
    var snappedDate by remember { mutableStateOf(initialDate) }

    val dayOfMonths = snappedDate.calculateDayOfMonths()

    val months = (1..12).mapIndexed { index, item ->
        Item(
            text = Month(item).name.lowercase().capitalize,
            value = item,
            index = index
        )
    }

    val years = (minDate.year..maxDate.year).mapIndexed { index, item ->
        Item(
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
            // DayOfMonth
            key(dayOfMonths) {
                WheelTextPicker(
                    modifier = Modifier.weight(1F),
                    endless = endless,
                    texts = dayOfMonths.map { it.text },
                    initialIndex = dayOfMonths.find { it.value == snappedDate.dayOfMonth }?.index ?: 0
                ) { index ->
                    snappedDate = snappedDate.withDayOfMonth(dayOfMonths[index].value)
                    return@WheelTextPicker dayOfMonths.find { it.value == snappedDate.dayOfMonth }?.index
                }
            }

            // Month
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = months.map { it.text },
                initialIndex = months.find { it.value == snappedDate.monthNumber }?.index ?: 0
            ) { index ->
                snappedDate = snappedDate.withMonth(months[index].value)
                return@WheelTextPicker months.find { it.value == snappedDate.monthNumber }?.index
            }

            // Year
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = years.map { it.text },
                initialIndex = years.find { it.value == snappedDate.year }?.index ?: 0
            ) { index ->
                snappedDate = snappedDate.withYear(years[index].value)
                return@WheelTextPicker years.find { it.value == snappedDate.year }?.index
            }
        }
    }
}

private data class Item(
    val text: String,
    val value: Int,
    val index: Int,
)

enum class DateFormat {
    DAY_MONTH_YEAR,
    MONTH_DAY_YEAR,
    YEAR_MONTH_DAY,
}

private fun LocalDate.calculateDayOfMonths(): List<Item> {
    val days = when (monthNumber) {
        2 -> if (isLeapYear) 29 else 28

        1, 3, 5, 7, 8, 10, 12 -> 31

        4, 6, 9, 11 -> 30

        else -> 0
    }

    return (1..days).mapIndexed { index, item ->
        Item(
            text = item.toString(),
            value = item,
            index = index
        )
    }
}
