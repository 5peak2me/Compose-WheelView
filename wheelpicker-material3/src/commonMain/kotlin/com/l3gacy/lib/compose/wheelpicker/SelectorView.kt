package com.l3gacy.lib.compose.wheelpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

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
//                .alpha(0.5F)
//                .background(MaterialTheme.colors.surface)
//                .background(color = Color(0x80F1F1F1), shape = MaterialTheme.shapes.medium)
//                .background(color = Color.LightGray, shape = MaterialTheme.shapes.medium)
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
//                    .background(MaterialTheme.colors.primary)
//                    .fillMaxWidth()
//            )
//            Box(
//                modifier = Modifier
//                    .height(0.5.dp)
//                    .alpha(0.5f)
//                    .background(MaterialTheme.colors.primary)
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
