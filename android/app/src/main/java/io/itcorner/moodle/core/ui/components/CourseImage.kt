package io.itcorner.moodle.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

private val palette = listOf(
    Color(0xFFF98012) to Color(0xFFB85F08),
    Color(0xFF1976D2) to Color(0xFF0D47A1),
    Color(0xFF388E3C) to Color(0xFF1B5E20),
    Color(0xFF8E24AA) to Color(0xFF4A148C),
    Color(0xFFD32F2F) to Color(0xFF8E0000),
    Color(0xFF00897B) to Color(0xFF004D40),
)

@Composable
fun CourseImage(
    url: String?,
    fallbackText: String,
    seed: Long,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    val idx = ((seed % palette.size).toInt() + palette.size) % palette.size
    val (start, end) = palette[idx]

    val placeholder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(start, end))),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                fallbackText.take(2).uppercase(),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }

    val frame = modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
        .clip(shape)

    if (url.isNullOrBlank()) {
        Box(frame) { placeholder() }
    } else {
        SubcomposeAsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = frame,
            loading = { placeholder() },
            error = { placeholder() },
        )
    }
}
