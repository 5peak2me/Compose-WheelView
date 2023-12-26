package com.l3gacy.lib.compose.wheelview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Properties used to customize the behavior of a [WheelView].
 *
 * @property haptic Enable haptic feedback
 * @property scrollable Enable scrolling
 * @property endless Enable endless scrolling
 */
@Immutable
class WheelViewProperties(
    val offset: Int = 2,
    val haptic: Boolean = true,
    val scrollable: Boolean = true,
    val endless: Boolean = true,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WheelViewProperties) return false

        if (offset != other.offset) return false
        if (haptic != other.haptic) return false
        if (scrollable != other.scrollable) return false

        return endless == other.endless
    }

    override fun hashCode(): Int {
        var result = offset.hashCode()
        result = 31 * result + haptic.hashCode()
        result = 31 * result + endless.hashCode()
        result = 31 * result + scrollable.hashCode()
        return result
    }
}

@Composable
fun WheelView(
    modifier: Modifier = Modifier,
    itemSize: DpSize = DpSize(256.dp, 40.dp),
    selection: Int = 0,
    itemCount: Int,
    rowOffset: Int = 2,
    onSelectionChanged: (Int) -> Unit,
    selectorOption: SelectorOptions = SelectorOptions(),
    lazyWheelState: LazyListState? = null,
    properties: WheelViewProperties = WheelViewProperties(),
    itemContent: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val count = if (properties.endless) itemCount else itemCount + 2 * rowOffset
    val rowOffsetCount = maxOf(1, minOf(rowOffset, 4))
    // visible item count for WheelView
    val visibleCount = (rowOffsetCount * 2) + 1
    val startIndex =
        if (properties.endless) selection + (itemCount * 1000) - rowOffset else selection

    val state = lazyWheelState ?: rememberLazyListState(startIndex)

    val size = DpSize(itemSize.width, itemSize.height * visibleCount)

    val isScrollInProgress = state.isScrollInProgress
    val selectedIndex = remember {
        derivedStateOf { state.firstVisibleItemIndex + rowOffsetCount }
    }

    LaunchedEffect(key1 = itemCount) {
        coroutineScope.launch {
            state.scrollToItem(startIndex)
        }
    }

    LaunchedEffect(key1 = isScrollInProgress) {
        if (!isScrollInProgress) {
            calculateSelectedIndex(state, size.height).let {
                val index = if (properties.endless) {
                    (it + rowOffsetCount) % itemCount
                } else {
                    (it + rowOffsetCount) % count - rowOffset
                }
                println("index: $index")
                onSelectionChanged(index)
                if (state.firstVisibleItemScrollOffset != 0) {
                    coroutineScope.launch {
                        state.animateScrollToItem(it, 0)
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow(state::firstVisibleItemIndex).collect {
            if (properties.haptic) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(modifier = modifier.height(size.height).fillMaxWidth()) {
        if (selectorOption.enabled) {
            SelectionView(
                modifier.align(Alignment.Center).padding(horizontal = 16.dp).fillMaxWidth()
                    .height(itemSize.height),
            )
        }
        LazyColumn(
            modifier = Modifier.height(size.height).fillMaxWidth(),
            state = state,
            userScrollEnabled = properties.scrollable,
        ) {
            items(if (properties.endless) Int.MAX_VALUE else count) {
                val rotateDegree = calculateRotationByIndex(selectedIndex.value, it, rowOffset)
                Box(
                    modifier = Modifier
                        .height(itemSize.height)
                        .fillMaxWidth()
                        .graphicsLayer {
//                            this.rotationX = rotateDegree
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (properties.endless) {
                        itemContent(it % itemCount)
                    } else if (it >= rowOffsetCount && it < itemCount + rowOffsetCount) {
                        itemContent((it - rowOffsetCount) % itemCount)
                    }
                }
            }
        }
    }

}

@Composable
private fun SelectionView(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier.background(color = Color(0xFFF1F1F1), shape = MaterialTheme.shapes.medium)
            .fillMaxSize()
    )
}

private fun calculateSelectedIndex(listState: LazyListState, height: Dp): Int {
    val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull()
    var index = currentItem?.index ?: 0

    if (currentItem?.offset != 0) {
        println(currentItem?.offset)
        if (currentItem != null && currentItem.offset <= -height.value * 3 / 10) {
            index++
        }
    }
    return index
}

private fun calculateRotationByIndex(focusedIndex: Int, index: Int, offset: Int): Float {
    return (6 * offset + 1).toFloat() * (focusedIndex - index)
}
