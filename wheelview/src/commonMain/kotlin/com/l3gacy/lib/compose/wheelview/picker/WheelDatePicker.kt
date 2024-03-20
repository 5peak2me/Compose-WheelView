package com.l3gacy.lib.compose.wheelview.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelview.WheelView
import com.l3gacy.lib.compose.wheelview.picker.internal.EPOCH
import com.l3gacy.lib.compose.wheelview.picker.internal.MAX
import com.l3gacy.lib.compose.wheelview.picker.internal.capitalize
import com.l3gacy.lib.compose.wheelview.picker.internal.isAfter
import com.l3gacy.lib.compose.wheelview.picker.internal.isBefore
import com.l3gacy.lib.compose.wheelview.picker.internal.now
import com.l3gacy.lib.compose.wheelview.picker.internal.withMonth
import com.l3gacy.lib.compose.wheelview.picker.internal.withYear
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
    onSelectedDate: (LocalDate) -> Unit,
) {
    var snappedDate by remember { mutableStateOf(initialDate) }

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
            WheelView(modifier = Modifier.weight(1F), itemCount = 12, onSelectionChanged = {}) {

            }

            // Month
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                startIndex = months.find { it.value == snappedDate.monthNumber }?.index ?: 0,
                texts = months.map { it.text },
                onScrollFinished = { index ->
                    val newMonth = months.find { it.index == index }?.value

                    newMonth?.let {
                        val newDate = snappedDate.withMonth(newMonth)

                        if (!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                            snappedDate = newDate
                        }

                        val newIndex = months.find { it.value == snappedDate.monthNumber }?.index

                        newIndex?.let {

                        }
                    }
                    return@WheelTextPicker months.find { it.index == snappedDate.monthNumber }?.index
                }
            )

            // Year
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                texts = years.map { it.text },
                startIndex = years.find { it.value == snappedDate.year }?.index ?: 0,
                onScrollFinished = { index ->
                    val newYear = years.find { it.index == index }?.value

                    newYear?.let {
                        val newDate = snappedDate.withYear(newYear)

                        if (!newDate.isBefore(minDate) && !newDate.isAfter(maxDate)) {
                            snappedDate = newDate
                        }
                    }
                    return@WheelTextPicker years.find { it.index == snappedDate.year }?.index
                }
            )
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
