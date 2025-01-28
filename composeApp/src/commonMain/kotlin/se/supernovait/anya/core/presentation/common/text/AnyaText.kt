package se.supernovait.anya.core.presentation.common.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun AnyaLabel(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
    Text(
        text = text,
        fontSize = fontSize,
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        modifier = Modifier
            .padding(0.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaBoldLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = style,
        color = color,
        modifier = Modifier
            .padding(0.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaCopyright(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = Modifier
            .padding(0.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaQuote(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        modifier = Modifier
            .padding(4.dp)
            .then(modifier)
    )
}

@Composable
fun AnyaTag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize
) {
    Box(modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(6.dp))
        .background(color)
        .then(modifier)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(8.dp, 4.dp)
        )
    }
}
