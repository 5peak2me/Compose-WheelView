package com.l3gacy.lib.compose.wheelpicker

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * @see [android.text.format.DateFormat.is24HourFormat]
 */
internal actual val is24HourFormat: Boolean
    @Composable
    @ReadOnlyComposable get() = DateFormat.is24HourFormat(LocalContext.current)

/**
 * @see [android.text.format.DateFormat.getDateFormatOrder]
 */
@Composable
internal actual fun getDateFormatOrder(): CharArray {
    return DateFormat.getDateFormatOrder(LocalContext.current)
}
