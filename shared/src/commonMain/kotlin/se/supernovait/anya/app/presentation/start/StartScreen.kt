package se.supernovait.anya.app.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.auth_action_sign_out_label
import anya.shared.generated.resources.ic_cat
import anya.shared.generated.resources.ic_censored_content
import anya.shared.generated.resources.ic_info
import anya.shared.generated.resources.ic_logout
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.ic_user_group
import anya.shared.generated.resources.screen_Start_action_cat_content_description
import anya.shared.generated.resources.screen_Start_action_cat_label
import anya.shared.generated.resources.screen_Start_action_censor_label
import anya.shared.generated.resources.screen_Start_action_info_content_description
import anya.shared.generated.resources.screen_Start_action_info_label
import anya.shared.generated.resources.screen_Start_action_owner_content_description
import anya.shared.generated.resources.screen_Start_action_owner_label
import anya.shared.generated.resources.user_profile_action_content_description
import anya.shared.generated.resources.user_profile_label
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.container.ScreenContainer
import se.supernovait.anya.core.presentation.common.menu.MainMenuItem
import se.supernovait.anya.core.presentation.common.menu.MainMenuItemGroup
import se.supernovait.anya.core.presentation.common.menu.PrimeMenuItem
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

/**
 * Composable that represents the user's start screen
 * @param onEvent lambda that triggers different navigation actions
 */
@Composable
fun StartScreen(onEvent: (StartScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val signOutButtonLabel = stringResource(Res.string.auth_action_sign_out_label)

    ScreenContainer(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                PrimeMenuItem(
                    icon = Res.drawable.ic_cat,
                    label = stringResource(Res.string.screen_Start_action_cat_label),
                    contentDescription = stringResource(Res.string.screen_Start_action_cat_content_description),
                    style = AnyaActionDefaults.defaultStyle,
                    onClick = { onEvent(StartScreenEvent.NavigateToCatScreen) }
                )
            }
            item {
                PrimeMenuItem(
                    icon = Res.drawable.ic_user_group,
                    label = stringResource(Res.string.screen_Start_action_owner_label),
                    contentDescription = stringResource(Res.string.screen_Start_action_owner_content_description),
                    style = AnyaActionDefaults.defaultStyle,
                    onClick = { onEvent(StartScreenEvent.NavigateToOwnerScreen) }
                )
            }
            item {
                PrimeMenuItem(
                    icon = Res.drawable.ic_person,
                    label = stringResource(Res.string.user_profile_label),
                    contentDescription = stringResource(Res.string.user_profile_action_content_description),
                    style = AnyaActionDefaults.defaultStyle,
                    onClick = { onEvent(StartScreenEvent.NavigateToProfileScreen) }
                )
            }
            item {
                PrimeMenuItem(
                    icon = Res.drawable.ic_info,
                    label = stringResource(Res.string.screen_Start_action_info_label),
                    contentDescription = stringResource(Res.string.screen_Start_action_info_content_description),
                    style = AnyaActionDefaults.defaultStyle,
                    onClick = { onEvent(StartScreenEvent.NavigateToInfoScreen) }
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.spacing.medium))

        MainMenuItemGroup {
            MainMenuItem(
                icon = Res.drawable.ic_censored_content,
                label = stringResource(Res.string.screen_Start_action_censor_label),
                contentDescription = stringResource(Res.string.screen_Start_action_info_content_description),
                style = AnyaActionDefaults.defaultStyle,
                onClick = { onEvent(StartScreenEvent.NavigateToCensoredTextScreen) }
            )
            MainMenuItem(
                icon = Res.drawable.ic_logout,
                label = signOutButtonLabel,
                contentDescription = "$signOutButtonLabel $a11yButtonText",
                style = AnyaActionDefaults.defaultStyle,
                onClick = { onEvent(StartScreenEvent.SignOut) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        StartScreen({ })
    }
}
