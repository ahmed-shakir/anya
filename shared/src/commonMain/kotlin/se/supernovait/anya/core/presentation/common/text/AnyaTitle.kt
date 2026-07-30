package se.supernovait.anya.core.presentation.common.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AnyaTitle(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        color = color,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaSubtitle(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        color = color,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaHeading(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    align: TextAlign? = null,
) {
    HeadingText(
        text = text,
        color = color,
        style = style,
        align = align,
        modifier = modifier
    )
}

@Composable
private fun HeadingText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    style: TextStyle,
    align: TextAlign? = null
) {
    Text(
        text = text,
        style = style,
        textAlign = align,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}
