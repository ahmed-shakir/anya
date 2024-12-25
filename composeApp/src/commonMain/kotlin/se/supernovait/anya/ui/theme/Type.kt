package se.supernovait.anya.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.space_mono_bold
import anya.composeapp.generated.resources.space_mono_bold_italic
import anya.composeapp.generated.resources.space_mono_italic
import anya.composeapp.generated.resources.space_mono_regular
import org.jetbrains.compose.resources.Font

@Composable
fun SpaceMono() = FontFamily(
    Font(
        resource = Res.font.space_mono_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.space_mono_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resource = Res.font.space_mono_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resource = Res.font.space_mono_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
)

@Composable
fun Typography() = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = SpaceMono()),
        displayMedium = displayMedium.copy(fontFamily = SpaceMono()),
        displaySmall = displaySmall.copy(fontFamily = SpaceMono()),
        headlineLarge = headlineLarge.copy(fontFamily = SpaceMono()),
        headlineMedium = headlineMedium.copy(fontFamily = SpaceMono()),
        headlineSmall = headlineSmall.copy(fontFamily = SpaceMono()),
        titleLarge = titleLarge.copy(fontFamily = SpaceMono()),
        titleMedium = titleMedium.copy(fontFamily = SpaceMono()),
        titleSmall = titleSmall.copy(fontFamily = SpaceMono()),
        bodyLarge = bodyLarge.copy(fontFamily = SpaceMono()),
        bodyMedium = bodyMedium.copy(fontFamily = SpaceMono()),
        bodySmall = bodySmall.copy(fontFamily = SpaceMono()),
        labelLarge = labelLarge.copy(fontFamily = SpaceMono()),
        labelMedium = labelMedium.copy(fontFamily = SpaceMono()),
        labelSmall = labelSmall.copy(fontFamily = SpaceMono())
    )
}
