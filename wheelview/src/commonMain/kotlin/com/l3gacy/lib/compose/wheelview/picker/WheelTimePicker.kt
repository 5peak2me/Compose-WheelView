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
import com.l3gacy.lib.compose.wheelview.picker.internal.MAX
import com.l3gacy.lib.compose.wheelview.picker.internal.MIN
import com.l3gacy.lib.compose.wheelview.picker.internal.isAfter
import com.l3gacy.lib.compose.wheelview.picker.internal.isBefore
import com.l3gacy.lib.compose.wheelview.picker.internal.now
import com.l3gacy.lib.compose.wheelview.picker.internal.withHour
import com.l3gacy.lib.compose.wheelview.picker.internal.withMinute
import com.l3gacy.lib.compose.wheelview.picker.internal.withSecond
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    initialTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    endless: Boolean = false,
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
    val amPmHours = (1..12).map {
        AmPmHour(
            text = it.toString(),
            value = it,
            index = it - 1
        )
    }

    val minutes = (0..59).map {
        Minute(
            text = it.toString().padStart(2, '0'),
            value = it,
            index = it
        )
    }

    val amPms = listOf(
        AmPm(
            text = "AM",
            value = AmPmValue.AM,
            index = 0
        ),
        AmPm(
            text = "PM",
            value = AmPmValue.PM,
            index = 1
        )
    )

    var snappedAmPm by remember {
        mutableStateOf(
            amPms.find { it.value == amPmValueFromTime(initialTime) } ?: amPms[0]
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row {
            if (timeFormat == TimeFormat.HOUR_12) {
                WheelTextPicker(
                    modifier = Modifier.weight(1F),
                    endless = endless,
                    texts = amPms.map { it.text },
                    initialIndex = amPms.find { it.value == amPmValueFromTime(initialTime) }?.index ?: 0
                ) { snappedIndex ->
                    val newAmPm = amPms.find {
                        if (snappedIndex == 2) {
                            it.index == 1
                        } else {
                            it.index == snappedIndex
                        }
                    }

                    newAmPm?.let {
                        snappedAmPm = newAmPm
                    }

                    val newMinute = minutes.find { it.value == snappedTime.minute }?.value

                    val newHour = amPmHourToHour24(
                        amPmHours.find { it.value == localTimeToAmPmHour(snappedTime) }?.value ?: 0,
                        snappedTime.minute,
                        snappedAmPm.value
                    )

                    newMinute?.let {
                        val newTime = snappedTime.withMinute(newMinute).withHour(newHour)

                        if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                            snappedTime = newTime
                        }

                        val newIndex = minutes.find { it.value == snappedTime.hour }?.index

                        newIndex?.let {
                            onSelectedTime.invoke(snappedTime)
                        }
                    }

                    return@WheelTextPicker snappedIndex
                }
            }

            // Hour
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = if (timeFormat == TimeFormat.HOUR_24) hours.map { it.text } else amPmHours.map { it.text },
                initialIndex = if (timeFormat == TimeFormat.HOUR_24) {
                    hours.find { it.value == initialTime.hour }?.index ?: 0
                } else amPmHours.find { it.value == localTimeToAmPmHour(initialTime) }?.index ?: 0
            ) { snappedIndex ->
                val newHour = if (timeFormat == TimeFormat.HOUR_24) {
                    hours.find { it.index == snappedIndex }?.value
                } else {
                    amPmHourToHour24(
                        amPmHours.find { it.index == snappedIndex }?.value ?: 0,
                        snappedTime.minute,
                        snappedAmPm.value
                    )
                }
                newHour?.let {
                    val newTime = snappedTime.withHour(newHour)

                    if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                        snappedTime = newTime
                    }

                    val newIndex = if (timeFormat == TimeFormat.HOUR_24) {
                        hours.find { it.value == snappedTime.hour }?.index
                    } else {
                        amPmHours.find { it.value == localTimeToAmPmHour(snappedTime) }?.index
                    }

                    newIndex?.let {
                        onSelectedTime.invoke(snappedTime.withHour(it))
                    }
                }
                return@WheelTextPicker if (timeFormat == TimeFormat.HOUR_24) {
                    hours.find { it.value == snappedTime.hour }?.index
                } else {
                    amPmHours.find { it.value == localTimeToAmPmHour(snappedTime) }?.index
                }
            }

            // Minute
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = minutes.map { it.text },
                initialIndex = minutes.find { it.value == initialTime.minute }?.index ?: 0
            ) { snappedIndex ->
                val newMinute = minutes.find { it.index == snappedIndex }?.value

                val newHour = if (timeFormat == TimeFormat.HOUR_24) {
                    hours.find { it.value == snappedTime.hour }?.value
                } else {
                    amPmHourToHour24(
                        amPmHours.find { it.value == localTimeToAmPmHour(snappedTime) }?.value ?: 0,
                        snappedTime.minute,
                        snappedAmPm.value
                    )
                }

                newMinute?.let {
                    newHour?.let {
                        val newTime = snappedTime.withMinute(newMinute).withHour(newHour)

                        if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                            snappedTime = newTime
                        }

                        val newIndex = minutes.find { it.value == snappedTime.minute }?.index

                        newIndex?.let {
                            onSelectedTime.invoke(snappedTime.withMinute(it))
                        }
                    }
                }

                return@WheelTextPicker minutes.find { it.value == snappedTime.minute }?.index
            }

            // Second
            WheelTextPicker(
                modifier = Modifier.weight(1F),
                endless = endless,
                texts = minutes.map { it.text },
                initialIndex = minutes.find { it.value == initialTime.second }?.index ?: 0,
            ) { snappedIndex ->

                val newSecond = minutes.find { it.index == snappedIndex }?.value

                newSecond?.let {
                    val newTime = snappedTime.withSecond(newSecond)

                    if (!newTime.isBefore(minTime) && !newTime.isAfter(maxTime)) {
                        snappedTime = newTime
                    }

                    val newIndex = minutes.find { it.value == snappedTime.second }?.index

                    newIndex?.let {
                        onSelectedTime.invoke(snappedTime.withSecond(it))
                    }
                }


                return@WheelTextPicker minutes.find { it.value == snappedTime.second }?.index
            }
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

private data class AmPmHour(
    val text: String,
    val value: Int,
    val index: Int
)

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
