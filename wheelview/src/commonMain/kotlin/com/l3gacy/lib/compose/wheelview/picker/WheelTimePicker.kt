package com.l3gacy.lib.compose.wheelview.picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelview.WheelView
import com.l3gacy.lib.compose.wheelview.picker.internal.now
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime = LocalTime.now(),
    minTime: LocalTime,
    maxTime: LocalTime,
    endless: Boolean = false,
    timeFormat: TimeFormat = TimeFormat.HOURS_24,
    onSelectedTime: (LocalTime) -> Unit,
) {

    Box(modifier = modifier) {

        Row {
            if (timeFormat == TimeFormat.HOURS_12) {
                WheelView(
                    modifier = Modifier.weight(1F), itemCount = 12,
                    onSelectionChanged = {

                    },
                ) {

                }
            }
            WheelView(
                modifier = Modifier.weight(1F), itemCount = 24,
                onSelectionChanged = {

                },
            ) {

            }
            WheelView(
                modifier = Modifier.weight(1F), itemCount = 24,
                onSelectionChanged = {

                },
            ) {

            }
        }

    }
}

enum class TimeFormat {
    HOURS_12,
    HOURS_24,
}
