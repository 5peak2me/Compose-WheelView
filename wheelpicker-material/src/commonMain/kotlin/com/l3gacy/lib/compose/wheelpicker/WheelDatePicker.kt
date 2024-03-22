package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelpicker.internal.EPOCH
import com.l3gacy.lib.compose.wheelpicker.internal.MAX
import com.l3gacy.lib.compose.wheelpicker.internal.capitalize
import com.l3gacy.lib.compose.wheelpicker.internal.isAfter
import com.l3gacy.lib.compose.wheelpicker.internal.isBefore
import com.l3gacy.lib.compose.wheelpicker.internal.isLeapYear
import com.l3gacy.lib.compose.wheelpicker.internal.now
import com.l3gacy.lib.compose.wheelpicker.internal.withDayOfMonth
import com.l3gacy.lib.compose.wheelpicker.internal.withMonth
import com.l3gacy.lib.compose.wheelpicker.internal.withYear
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number

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

    var dayOfMonths = calculateDayOfMonths(snappedDate.month.number, snappedDate.year)

    val months = (1..12).map {
        val monthName = Month(it).name.lowercase().capitalize

        Item(
            text = monthName,
            value = it,
            index = it - 1
        )
    }

    val years = (minDate.year..maxDate.year).mapIndexed { index, item ->
        Item(
            text = item.toString(),
            value = item,
            index = index
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            // Day
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = dayOfMonths.map { it.text },
                initialIndex = dayOfMonths.find { it.value == initialDate.dayOfMonth }?.index ?: 0,
            ) { snappedIndex ->
                val newDayOfMonth = dayOfMonths.find { it.index == snappedIndex }?.value
                newDayOfMonth?.let {
                    val newDate = snappedDate.withDayOfMonth(newDayOfMonth)

                    if (!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                        snappedDate = newDate
                    }

                    val newIndex = dayOfMonths.find { it.value == snappedDate.dayOfMonth }?.index

                    newIndex?.let {
                        onSelectedDate.invoke(newDate)
                    }
                }

                return@WheelTextPicker dayOfMonths.find { it.value == snappedDate.dayOfMonth }?.index
            }

            // Month
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = months.map { it.text },
                initialIndex = months.find { it.value == initialDate.monthNumber }?.index ?: 0
            ) { snappedIndex ->

                val newMonth = months.find { it.index == snappedIndex }?.value

                newMonth?.let {
                    val newDate = snappedDate.withMonth(newMonth)

                    if (!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                        snappedDate = newDate
                    }

//                    dayOfMonths = calculateDayOfMonths(snappedDate.month.number, snappedDate.year)

                    val newIndex = months.find { it.value == snappedDate.monthNumber }?.index

                    newIndex?.let {
                        onSelectedDate.invoke(newDate)
                    }
                }
                return@WheelTextPicker months.find { it.value == snappedDate.monthNumber }?.index
            }

            // Year
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = years.map { it.text },
                initialIndex = years.find { it.value == initialDate.year }?.index ?: 0
            ) { index ->
                val newYear = years.find { it.index == index }?.value

                newYear?.let {
                    val newDate = snappedDate.withYear(newYear)

                    if (!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                        snappedDate = newDate
                    }

//                    dayOfMonths = calculateDayOfMonths(snappedDate.month.number, snappedDate.year)

                    val newIndex = years.find { it.value == snappedDate.year }?.index

                    newIndex?.let {
                        onSelectedDate.invoke(newDate)
                    }
                }
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

private fun calculateDayOfMonths(month: Int, year: Int): List<Item> {

    val isLeapYear = LocalDate(year, month, 1).isLeapYear

    val days = when (month) {
        2 -> if (isLeapYear) 29 else 28

        1, 3, 5, 7, 8, 10, 12 -> 31

        4, 6, 9, 11 -> 30

        else -> 0
    }

    return (1..days).map {
        Item(
            text = it.toString(),
            value = it,
            index = it - 1
        )
    }
}
