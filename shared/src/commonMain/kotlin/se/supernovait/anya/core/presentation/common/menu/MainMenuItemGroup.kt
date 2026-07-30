package se.supernovait.anya.core.presentation.common.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.user_profile_action_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing

@Composable
fun MainMenuItemGroup(content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.divider),
        modifier = Modifier.clip(shape = RoundedCornerShape(8))
    ) {
        content()
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    MainMenuItemGroup {
        MainMenuItem(
            label = "Item 1",
            icon = Res.drawable.ic_person,
            contentDescription = stringResource(Res.string.user_profile_action_content_description),
            onClick = { }
        )
    }
}
