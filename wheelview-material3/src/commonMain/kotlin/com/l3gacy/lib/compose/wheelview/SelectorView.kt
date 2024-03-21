package com.l3gacy.lib.compose.wheelview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent

/**
 * Default [SelectorView] implementation.
 */
@Composable
internal fun SelectorView(modifier: Modifier = Modifier, offset: Int) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Box(
            modifier = modifier
                .weight(offset.toFloat())
                .fillMaxWidth()
                .alpha(0.5F)
                .background(MaterialTheme.colorScheme.surface),
        )

        Box(
            modifier = modifier
                .weight(1.13f)
//                .padding(horizontal = 12.dp)
                .fillMaxWidth()
//                .background(color = Color(0xFFF1F1F1), shape = MaterialTheme.shapes.medium)
//                .graphicsLayer {
//                    compositingStrategy = CompositingStrategy.Offscreen
//                    alpha = 0.1F
//                    clip = true
//                    cameraDistance = 8F
//                }
                .drawWithContent {
//                    drawRect(
//                        color = colorLightOnBackground,
//                        blendMode = BlendMode.DstOver
//                    )
//                    drawRect(
//                        color = Color.Transparent,
//                        blendMode = BlendMode.DstOver
//                    )
//                    drawContent()
                }
//                .drawBehind {
//                    drawRect(
//                        color = Color(0xFFF1F1F1),
//                        size = size
//                    )
//                }
//                .clip(MaterialTheme.shapes.medium)
        )

//        Column(
//            modifier = modifier
//                .weight(1.13f)
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(0.5.dp)
//                    .alpha(0.5f)
//                    .background(if (darkModeEnabled) MaterialTheme.colors.primary else colorLightTextPrimary)
//                    .fillMaxWidth()
//            )
//            Box(
//                modifier = Modifier
//                    .height(0.5.dp)
//                    .alpha(0.5f)
//                    .background(if (darkModeEnabled) MaterialTheme.colors.primary else colorLightTextPrimary)
//                    .fillMaxWidth()
//            )
//        }

        Box(
            modifier = modifier
                .weight(offset.toFloat())
                .fillMaxWidth()
                .alpha(0.5F)
                .background(MaterialTheme.colorScheme.surface),
        )
    }
}
