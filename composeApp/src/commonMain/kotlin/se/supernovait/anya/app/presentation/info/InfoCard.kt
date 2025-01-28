package se.supernovait.anya.app.presentation.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import se.supernovait.anya.core.presentation.common.text.AnyaCopyright
import se.supernovait.anya.ui.theme.AnyaTheme
import se.supernovait.anya.ui.theme.spacing

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(MaterialTheme.spacing.medium)
            .shadow(
                elevation = MaterialTheme.spacing.medium,
                shape = MaterialTheme.shapes.extraSmall,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        shape = MaterialTheme.shapes.extraSmall,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = contentColor
        )
    ) {
        Column(Modifier.padding(MaterialTheme.spacing.medium)) {
            content()
        }
    }
}

@Preview
@Composable
private fun InfoCardPreview() {
    AnyaTheme {
        InfoCard {
            AnyaCopyright(text = "Test card")
        }
    }
}
