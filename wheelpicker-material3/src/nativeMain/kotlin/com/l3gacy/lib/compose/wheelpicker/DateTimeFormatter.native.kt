package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.runtime.Composable

/**
 * @see [androidx.compose.material3.is24HourFormat]
 */
internal actual val is24HourFormat: Boolean
    get() = TODO("Not yet implemented")

/**
 * Gets the current date format stored as a char array. Returns a 3 element
 * array containing the day ('d'), month ('M'), and year ('y'))
 * in the order specified by the user's format preference.  Note that this order is
 * <i>only</i> appropriate for all-numeric dates; spelled-out (MEDIUM and LONG)
 * dates will generally contain other punctuation, spaces, or words,
 * not just the day, month, and year, and not necessarily in the same
 * order returned here.
 *
 */
@Composable
internal actual fun getDateFormatOrder(): CharArray {
    TODO("Not yet implemented")
}
