package com.l3gacy.lib.compose.wheelview.picker

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    startIndex: Int = 0,
    texts: List<String>,
    rowCount: Int = 7,
    style: TextStyle = MaterialTheme.typography.subtitle1,
    color: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onScrollFinished: (snappedIndex: Int) -> Int? = { null },
) {
    WheelPicker(
        modifier = modifier,
        startIndex = startIndex,
        count = texts.size,
        rowCount = rowCount,
        selectorProperties = selectorProperties,
        onScrollFinished = onScrollFinished
    ) { index ->
        Text(
            text = texts[index],
            style = style,
            color = color,
            maxLines = 1
        )
    }
}
