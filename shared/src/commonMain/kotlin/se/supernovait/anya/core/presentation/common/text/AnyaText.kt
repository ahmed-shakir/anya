package se.supernovait.anya.core.presentation.common.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon

@Composable
fun AnyaLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    textAlign: TextAlign = style.textAlign,
    fontWeight: FontWeight? = style.fontWeight,
    fontSize: TextUnit = style.fontSize
) {
    Text(
        text = text,
        color = color,
        style = style,
        textAlign = textAlign,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = modifier
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
    AnyaLabel(
        text = text,
        color = color,
        style = style,
        textAlign = TextAlign.Center,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        modifier = modifier
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
        modifier = modifier
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
fun AnyaIconText(
    text: StringResource,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
    ) {
        Text(text = stringResource(text), style = style, fontWeight = fontWeight, color = color)
        AnyaIcon(icon = icon, tint = color)
    }
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
