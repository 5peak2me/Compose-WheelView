package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.l3gacy.lib.compose.wheelview.SelectorProperties
import com.l3gacy.lib.compose.wheelview.WheelView
import com.l3gacy.lib.compose.wheelview.WheelPickerDefaults

@Composable
fun WheelTextPicker(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    texts: List<String>,
    endless: Boolean = true,
    style: TextStyle = MaterialTheme.typography.h6,
    color: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onScrollFinished: (snappedIndex: Int) -> Unit = { },
) {
    WheelView(
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
