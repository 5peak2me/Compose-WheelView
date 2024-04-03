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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelView(
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    itemCount: Int,
    rowOffset: Int = 3,
    endless: Boolean = true,
    onItemSelected: (index: Int) -> Unit = { },
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    val count = if (endless) itemCount else itemCount + 2 * rowOffset
    val rowOffsetCount = maxOf(1, minOf(rowOffset, 4))
    val rowCount = ((rowOffsetCount * 2) + 1)
    val startIndex = if (endless) initialIndex + (itemCount * 1000) - rowOffset else initialIndex

    val height = 32.dp * rowCount // Divisible by 3 / 5 / 7

    val singleViewPortHeight = height / rowCount
    val singleViewPortHeightToPx = singleViewPortHeight.toPx()

    val lazyListState = rememberLazyListState(startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val isScrollInProgress = lazyListState.isScrollInProgress

    LaunchedEffect(itemCount) {
        coroutineScope.launch {
            lazyListState.scrollToItem(startIndex)
        }
    }

    LaunchedEffect(isScrollInProgress) {
        if (!isScrollInProgress) {
            val index = calculateSnappedItemIndex(lazyListState)
            val snappedIndex = if (endless) {
                (index + rowOffsetCount) % itemCount
            } else {
                (index + rowOffsetCount) % count - rowOffset
            }

            onItemSelected(snappedIndex)
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .wheelPickerIndicator {
                    drawOutline(
                        outline = RoundedCornerShape(12.dp.toPx()).createOutline(Size(size.width, it), layoutDirection, this),
                        color = Color.White.copy(alpha = 0.5F),
                    )
                },
            state = lazyListState,
            flingBehavior = flingBehavior
        ) {
            items(if (endless) Int.MAX_VALUE else count) { index ->
                val (newAlpha, newRotationX) = calculateAnimatedAlphaAndRotationX(
                    lazyListState = lazyListState,
                    singleViewPortHeight = singleViewPortHeightToPx,
                    index = index,
                    rowCount = rowCount
                )

                Box(
                    modifier = Modifier
                        .height(singleViewPortHeight)
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

//        SelectorView(offset = rowOffset)

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
    singleViewPortHeight: Float,
    index: Int,
    rowCount: Int
): Pair<Float, Float> {

//    val layoutInfo = remember { derivedStateOf { lazyListState.layoutInfo } }.value
//    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
//    val singleViewPortHeight = viewPortHeight / rowCount

    val centerIndex =
        remember { derivedStateOf { lazyListState.firstVisibleItemIndex + rowCount / 2 } }.value
    val centerIndexOffset =
        remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }.value

    val distanceToCenterIndex = index - centerIndex

    val distanceToIndexSnap =
        distanceToCenterIndex * singleViewPortHeight.toInt() - centerIndexOffset
    val distanceToIndexSnapAbs = abs(distanceToIndexSnap)

    val animatedAlpha = if (distanceToIndexSnapAbs in 0..singleViewPortHeight.toInt()) {
        1.4f - (distanceToIndexSnapAbs / singleViewPortHeight)
    } else {
        0.4f
    }

    val animatedRotationX =
        (-20 * (distanceToIndexSnap / singleViewPortHeight)).takeUnless { it.isNaN() } ?: 0f

    return animatedAlpha to animatedRotationX
}

typealias CupertinoPickerIndicator = DrawScope.(itemHeight : Float) -> Unit

private fun Modifier.wheelPickerIndicator(
    indicator : CupertinoPickerIndicator
) = drawWithContent {
    drawContent()
    translate(0f, (size.height - 32.dp.toPx()) / 2) {
        indicator(32.dp.toPx())
    }
}

@Composable
private fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }
