package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.l3gacy.lib.compose.wheelview.SelectorProperties
import com.l3gacy.lib.compose.wheelview.WheelPicker
import com.l3gacy.lib.compose.wheelview.WheelPickerDefaults

@Composable
fun WheelTextPicker(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    texts: List<String>,
    endless: Boolean = true,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onScrollFinished: (snappedIndex: Int) -> Unit = { },
) {
    WheelPicker(
        modifier = modifier,
        initialIndex = initialIndex,
        itemCount = texts.size,
        endless = endless,
        selectorProperties = selectorProperties,
        onItemSelected = onScrollFinished
    ) { index ->
        Text(
            text = texts[index],
            style = style,
            color = color,
            maxLines = 1
        )
    }
}
