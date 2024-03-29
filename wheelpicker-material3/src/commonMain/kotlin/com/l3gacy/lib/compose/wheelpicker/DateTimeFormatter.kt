package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable

@Stable
interface DateTimeFormatter {
    fun is24HourFormat(): Boolean

}

/**
 * @see [androidx.compose.material3.is24HourFormat]
 */
internal expect val is24HourFormat: Boolean
    @Composable
    @ReadOnlyComposable get
