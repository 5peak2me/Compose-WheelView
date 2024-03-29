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
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    endless: Boolean = true,
    onSelectedTime: (LocalTime) -> Unit,
) {

    var snappedTime by remember { mutableStateOf(initialTime) }

    val hours = snappedTime.calculateHoursOfTime(minTime, maxTime)

    val minutes = snappedTime.calculateMinutesOfTime(minTime, maxTime)

    val seconds = snappedTime.calculateSecondsOfTime(minTime, maxTime)

    LaunchedEffect(snappedTime) {
        onSelectedTime(snappedTime)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            // Hour
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
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
                    endless = endless,
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
                    endless = endless,
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

private fun LocalTime.calculateHoursOfTime(minTime: LocalTime, maxTime: LocalTime): List<DateTime> =
    (minTime.hour..maxTime.hour).mapIndexed { index, item ->
        DateTime(
            text = item.toString().padding,
            value = item,
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
    }.mapIndexed { index, item ->
        DateTime(
            text = item.toString().padding,
            value = item,
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
    }.mapIndexed { index, item ->
        DateTime(
            text = item.toString().padding,
            value = item,
            index = index
        )
    }

internal fun localTimeToAmPmHour(localTime: LocalTime): Int {

    if (
        isBetween(
            localTime,
            LocalTime(0, 0),
            LocalTime(0, 59)
        )
    ) {
        return localTime.hour + 12
    }

    if (
        isBetween(
            localTime,
            LocalTime(1, 0),
            LocalTime(11, 59)
        )
    ) {
        return localTime.hour
    }

    if (
        isBetween(
            localTime,
            LocalTime(12, 0),
            LocalTime(12, 59)
        )
    ) {
        return localTime.hour
    }

    if (
        isBetween(
            localTime,
            LocalTime(13, 0),
            LocalTime(23, 59)
        )
    ) {
        return localTime.hour - 12
    }

    return localTime.hour
}

private fun isBetween(localTime: LocalTime, startTime: LocalTime, endTime: LocalTime): Boolean {
    return localTime in startTime..endTime
}

private fun amPmHourToHour24(amPmHour: Int, amPmMinute: Int, amPmValue: AmPmValue): Int {

    return when (amPmValue) {
        AmPmValue.AM -> {
            if (amPmHour == 12 && amPmMinute <= 59) {
                0
            } else {
                amPmHour
            }
        }

        AmPmValue.PM -> {
            if (amPmHour == 12 && amPmMinute <= 59) {
                amPmHour
            } else {
                amPmHour + 12
            }
        }
    }
}

private data class AmPm(
    val text: String,
    val value: AmPmValue,
    val index: Int?
)

internal enum class AmPmValue {
    AM, PM
}

private fun amPmValueFromTime(time: LocalTime): AmPmValue {
    return if (time.hour > 11) AmPmValue.PM else AmPmValue.AM
}

enum class TimeFormat {
    HOUR_12,
    HOUR_24,
}
