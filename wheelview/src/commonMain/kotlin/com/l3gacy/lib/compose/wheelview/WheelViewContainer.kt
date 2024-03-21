package com.l3gacy.lib.compose.wheelview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
internal fun WheelViewContainer(
    modifier: Modifier = Modifier,
    offset: Int,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(offset.toFloat())
                    .fillMaxWidth()
                    .alpha(0.5F)
                    .background(MaterialTheme.colors.surface),
            )

            Box(
                modifier = Modifier
                    .weight(1.13f)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .weight(offset.toFloat())
                    .fillMaxWidth()
                    .alpha(0.5F)
                    .background(MaterialTheme.colors.surface),
            )
        }
    }
}
