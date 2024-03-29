package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.l3gacy.lib.compose.wheelpicker.internal.DateTime
import com.l3gacy.lib.compose.wheelpicker.internal.MAX
import com.l3gacy.lib.compose.wheelpicker.internal.MIN
import com.l3gacy.lib.compose.wheelpicker.internal.now
import com.l3gacy.lib.compose.wheelpicker.internal.padding
import com.l3gacy.lib.compose.wheelpicker.internal.withHour
import com.l3gacy.lib.compose.wheelpicker.internal.withMinute
import com.l3gacy.lib.compose.wheelpicker.internal.withSecond
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    state: TimePickerState = rememberTimePickerState(),
    onSelectedTime: (LocalTime) -> Unit,
) {

    var snappedTime by remember { mutableStateOf(initialTime) }

    val hours = calculateHoursOfTime(minTime, maxTime, state)

    val minutes = snappedTime.calculateMinutesOfTime(minTime, maxTime)

    val seconds = snappedTime.calculateSecondsOfTime(minTime, maxTime)

    LaunchedEffect(snappedTime) {
        onSelectedTime(snappedTime)
    }

    val amPm = listOf("AM", "PM").mapIndexed { index, item ->
        DateTime(
            text = item,
            value = index,
            index = index
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
//            @OptIn(ExperimentalMaterial3Api::class)
//            TimePicker(
//                state = androidx.compose.material3.rememberTimePickerState()
//            )
            if (!state.is24Hour) {
                // AM/PM
                WheelTextPicker(
                    modifier = Modifier.weight(1F),
                    endless = state.endless,
                    texts = amPm.map { it.text },
                    initialIndex = hours.find { it.value == snappedTime.hour }?.index ?: 0
                ) { index ->
                    return@WheelTextPicker hours.find { it.value == snappedTime.hour }?.index
                }
            }
            // Hour
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = state.endless,
                texts = hours.map { it.text },
                initialIndex = hours.find { it.value == snappedTime.hour }?.index ?: 0
            ) { index ->
                snappedTime = snappedTime.withHour(hours[index].value)
                return@WheelTextPicker hours.find { it.value == snappedTime.hour }?.index
            }

            // Minute
            key(minutes) {
                WheelTextPicker(
                    modifier = Modifier.weight(1F),
                    endless = state.endless,
                    texts = minutes.map { it.text },
                    initialIndex = minutes.find { it.value == snappedTime.minute }?.index ?: 0
                ) { index ->
                    snappedTime = snappedTime.withMinute(minutes[index].value)
                    return@WheelTextPicker minutes.find { it.value == snappedTime.minute }?.index
                }
            }

            // Second
            key(seconds) {
                WheelTextPicker(
                    modifier = Modifier.weight(1F),
                    endless = state.endless,
                    texts = seconds.map { it.text },
                    initialIndex = seconds.find { it.value == snappedTime.second }?.index ?: 0,
                ) { index ->
                    snappedTime = snappedTime.withSecond(seconds[index].value)
                    return@WheelTextPicker seconds.find { it.value == snappedTime.second }?.index
                }
            }
        }

    }
}

@Composable
fun rememberTimePickerState(
    endless: Boolean = true,
    is24Hour: Boolean = is24HourFormat,
): TimePickerState = rememberSaveable(
    saver = TimePickerState.Saver()
) {
    TimePickerState(
        endless = endless,
        is24Hour = is24Hour,
    )
}

@Stable
class TimePickerState(
    val endless: Boolean = true,
    val is24Hour: Boolean,
) {
    fun hourForDisplay(hour: Int): Int = when {
        is24Hour -> hour % 24
        hour % 12 == 0 -> 12
        else -> hour - 12
    }

    companion object {
        /**
         * The default [Saver] implementation for [TimePickerState].
         */
        fun Saver(): Saver<TimePickerState, *> = Saver(
            save = {
                listOf(it.endless, it.is24Hour)
            },
            restore = { value ->
                TimePickerState(
                    endless = value[0],
                    is24Hour = value[1]
                )
            }
        )
    }
}

private fun calculateHoursOfTime(
    minTime: LocalTime,
    maxTime: LocalTime,
    state: TimePickerState
): List<DateTime> =
    (minTime.hour..maxTime.hour)
        .mapIndexed { index, hour ->
            DateTime(
                text = state.hourForDisplay(hour).toString().padding,
                value = hour,
                index = index
            )
        }

private fun LocalTime.calculateMinutesOfTime(
    minTime: LocalTime,
    maxTime: LocalTime
): List<DateTime> =
    when (hour) {
        minTime.hour -> minTime.minute..59
        maxTime.hour -> 0..maxTime.minute
        else -> 0..59
    }.mapIndexed { index, minute ->
        DateTime(
            text = minute.toString().padding,
            value = minute,
            index = index
        )
    }

private fun LocalTime.calculateSecondsOfTime(
    minTime: LocalTime,
    maxTime: LocalTime
): List<DateTime> =
    when {
        hour == minTime.hour && minute == minTime.minute -> minTime.second..59
        hour == maxTime.hour && minute == maxTime.minute -> 0..maxTime.second
        else -> 0..59
    }.mapIndexed { index, second ->
        DateTime(
            text = second.toString().padding,
            value = second,
            index = index
        )
    }
