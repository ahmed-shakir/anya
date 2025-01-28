package se.supernovait.anya.core.presentation.common.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AnyaTitle(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaSubtitle(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        modifier = Modifier
            .padding(16.dp, 0.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaHeading(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        style = MaterialTheme.typography.displayLarge,
        color = color,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaSubheading(modifier: Modifier = Modifier, text: String, color: Color = Color.Unspecified) {
    HeadingText(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = color,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .then(modifier)
    )
}

@Composable
private fun HeadingText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}
