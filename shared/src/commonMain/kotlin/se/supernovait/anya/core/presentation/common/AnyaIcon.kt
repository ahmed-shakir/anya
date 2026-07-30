package se.supernovait.anya.core.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnyaIcon(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: StringResource? = null,
    tint: Color = LocalContentColor.current,
    size: Dp = Dp.Unspecified
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription?.let { stringResource(it) },
        modifier = modifier.then(Modifier.size(size, size)),
        tint = tint
    )
}
