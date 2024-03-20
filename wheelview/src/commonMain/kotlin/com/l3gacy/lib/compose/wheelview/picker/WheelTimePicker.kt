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
import com.l3gacy.lib.compose.wheelview.picker.internal.MAX
import com.l3gacy.lib.compose.wheelview.picker.internal.MIN
import com.l3gacy.lib.compose.wheelview.picker.internal.now
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    endless: Boolean = false,
    timeFormat: TimeFormat = TimeFormat.HOURS_24,
    onSelectedTime: (LocalTime) -> Unit,
) {

    var snappedTime by remember { mutableStateOf(LocalTime(initialTime.hour, initialTime.minute)) }

    val hours = (0..23).map {
        Hour(
            text = it.toString().padStart(2, '0'),
            value = it,
            index = it
        )
    }

    val minutes = (0..59).map {
        Minute(
            text = it.toString().padStart(2, '0'),
            value = it,
            index = it
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            if (timeFormat == TimeFormat.HOURS_12) {
                WheelView(
                    modifier = Modifier.weight(1F), itemCount = 12,
                    onSelectionChanged = {

                    },
                ) {

                }
            }

            // Hour
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                texts = hours.map { it.text },
                startIndex = hours.find { it.value == initialTime.hour }?.index ?: 0,
            )

            // Minute
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                texts = minutes.map { it.text },
                startIndex = minutes.find { it.value == initialTime.minute }?.index ?: 0,
            )

            // Second
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                texts = minutes.map { it.text },
                startIndex = minutes.find { it.value == initialTime.second }?.index ?: 0,
            )
        }

    }
}

private data class Hour(
    val text: String,
    val value: Int,
    val index: Int
)

private data class Minute(
    val text: String,
    val value: Int,
    val index: Int
)

enum class TimeFormat {
    HOURS_12,
    HOURS_24,
}
