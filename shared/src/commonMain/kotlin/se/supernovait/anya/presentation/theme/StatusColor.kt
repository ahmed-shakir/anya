package se.supernovait.anya.app.presentation.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class StatusColor(
    val success: Color = Color(0xFF2E7D32),
    val warning: Color = Color(0xFFFFB84A),
    val error: Color = Color(0xFFBA1A1A)
)

val LocalStatus = staticCompositionLocalOf { StatusColor() }

val MaterialTheme.statusColor: StatusColor
    @Composable
    @ReadOnlyComposable
    get() = LocalStatus.current
