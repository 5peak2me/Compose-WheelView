package com.l3gacy.lib.compose.wheelview.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelview.WheelView
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    initialDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
    minDate: LocalDate,
    maxDate: LocalDate,
    dateFormat: DateFormat = DateFormat.DAY_MONTH_YEAR,
    onSelectedDate: (LocalDate) -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            WheelView(modifier = Modifier.weight(1F), itemCount = 12, onSelectionChanged = {}) {

            }
            WheelView(modifier = Modifier.weight(1F), itemCount = 12, onSelectionChanged = {}) {

            }
            WheelView(modifier = Modifier.weight(1F), itemCount = 12, onSelectionChanged = {}) {

            }
        }
    }
}

enum class DateFormat {
    DAY_MONTH_YEAR,
    MONTH_DAY_YEAR,
    YEAR_MONTH_DAY,
}
