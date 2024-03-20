package com.l3gacy.lib.compose.wheelview

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

/**
 * Represents the background and content colors used in a button in different states.
 *
 * See [ButtonDefaults.buttonColors] for the default colors used in a [Button].
 * See [ButtonDefaults.outlinedButtonColors] for the default colors used in a
 * [OutlinedButton].
 * See [ButtonDefaults.textButtonColors] for the default colors used in a [TextButton].
 */
@Stable
interface WheelViewColors {

    @Composable
    fun selectedColor(enabled: Boolean) : State<Color>

}

/**
 * Default [WheelViewColors] implementation.
 */
@Immutable
private class DefaultWheelViewColors(
    private val selectedColor: Color,
    private val disabledContentColor: Color
) : WheelViewColors {

    @Composable
    override fun selectedColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) selectedColor else disabledContentColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultWheelViewColors

        if (selectedColor != other.selectedColor) return false
        if (disabledContentColor != other.disabledContentColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = selectedColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }

}
