package com.l3gacy.lib.compose.wheelview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    itemCount: Int,
    offset: Int = 3,
    endless: Boolean = true,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onItemSelected: (index: Int) -> Int? = { null },
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    val count = if (endless) itemCount else itemCount + 2 * offset
    val rowOffsetCount = maxOf(1, minOf(offset, 4))
    val rowCount = ((rowOffsetCount * 2) + 1)
    val startIndex = if (endless) initialIndex + (itemCount * 1000) - offset else initialIndex

    val height = remember { 210.dp } // 3 * 5 * 7
    val lazyListState = rememberLazyListState(startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val isScrollInProgress = lazyListState.isScrollInProgress

    LaunchedEffect(isScrollInProgress, count) {
        if (!isScrollInProgress) {
            val index = calculateSnappedItemIndex(lazyListState)
            val snappedIndex = if (endless) {
                (index + rowOffsetCount) % itemCount
            } else {
                (index + rowOffsetCount) % count - offset
            }

            onItemSelected(snappedIndex)
            lazyListState.scrollToItem(index)
        }
    }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow(lazyListState::firstVisibleItemIndex).collect {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
//        if (selectorProperties.enabled().value) {
//            Surface(
//                modifier = Modifier
//                    .size(size.width, size.height / rowCount),
//                shape = selectorProperties.shape().value,
//                color = selectorProperties.color().value,
//                border = selectorProperties.border().value
//            ) {}
//        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            flingBehavior = flingBehavior
        ) {
            items(if (endless) Int.MAX_VALUE else count) { index ->
                val (newAlpha, newRotationX) = calculateAnimatedAlphaAndRotationX(
                    lazyListState = lazyListState,
                    index = index,
                    rowCount = rowCount
                )

                Box(
                    modifier = Modifier
                        .height(height / rowCount)
                        .fillMaxWidth()
                        .alpha(newAlpha)
                        .graphicsLayer {
                            rotationX = newRotationX
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (endless) {
                        content(index % itemCount)
                    } else if (index >= rowOffsetCount && index < itemCount + rowOffsetCount) {
                        content((index - rowOffsetCount) % itemCount)
                    }
                }
            }
        }

        SelectorView(offset = offset)

    }
}

private fun calculateSnappedItemIndex(lazyListState: LazyListState): Int {
    var currentItemIndex = lazyListState.firstVisibleItemIndex

    if (lazyListState.firstVisibleItemScrollOffset != 0) {
        currentItemIndex++
    }
    return currentItemIndex
}

@Composable
private fun calculateAnimatedAlphaAndRotationX(
    lazyListState: LazyListState,
    index: Int,
    rowCount: Int
): Pair<Float, Float> {

    val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
    val singleViewPortHeight = viewPortHeight / rowCount

    val centerIndex =
        remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }.value + rowCount / 2
    val centerIndexOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }.value

    val distanceToCenterIndex = index - centerIndex

    val distanceToIndexSnap =
        distanceToCenterIndex * singleViewPortHeight.toInt() - centerIndexOffset
    val distanceToIndexSnapAbs = abs(distanceToIndexSnap)

    val animatedAlpha = if (distanceToIndexSnapAbs in 0..singleViewPortHeight.toInt()) {
        1.2f - (distanceToIndexSnapAbs / singleViewPortHeight)
    } else {
        0.2f
    }

    val animatedRotationX =
        (-20 * (distanceToIndexSnap / singleViewPortHeight)).takeUnless { it.isNaN() } ?: 0f

    return animatedAlpha to animatedRotationX
}

object WheelPickerDefaults {
    @Composable
    fun selectorProperties(
        enabled: Boolean = true,
        shape: Shape = RoundedCornerShape(12.dp),
        color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    ): SelectorProperties = DefaultSelectorProperties(
        enabled = enabled,
        shape = shape,
        color = color,
        border = border
    )
}

@Stable
interface SelectorProperties {
    @Composable
    fun enabled(): State<Boolean>

    @Composable
    fun shape(): State<Shape>

    @Composable
    fun color(): State<Color>

    @Composable
    fun border(): State<BorderStroke?>
}

@Immutable
internal class DefaultSelectorProperties(
    private val enabled: Boolean,
    private val shape: Shape,
    private val color: Color,
    private val border: BorderStroke?
) : SelectorProperties {

    @Composable
    override fun enabled(): State<Boolean> {
        return rememberUpdatedState(enabled)
    }

    @Composable
    override fun shape(): State<Shape> {
        return rememberUpdatedState(shape)
    }

    @Composable
    override fun color(): State<Color> {
        return rememberUpdatedState(color)
    }

    @Composable
    override fun border(): State<BorderStroke?> {
        return rememberUpdatedState(border)
    }
}
