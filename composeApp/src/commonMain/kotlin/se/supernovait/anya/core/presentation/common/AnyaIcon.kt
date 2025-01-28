package se.supernovait.anya.core.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnyaIcon(
    icon: ImageVector,
    descriptionId: StringResource,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Dp = Dp.Unspecified
) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(descriptionId),
        modifier = modifier.then(Modifier.size(size)),
        tint = color
    )
}
