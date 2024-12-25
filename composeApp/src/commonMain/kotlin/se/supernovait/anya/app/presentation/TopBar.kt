package se.supernovait.anya.app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.app_name
import anya.composeapp.generated.resources.back_action_icon_description
import anya.composeapp.generated.resources.logo_description
import anya.composeapp.generated.resources.supernova_logo_star_color_v2
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.ui.theme.spacing

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(canNavigateBack: Boolean, navigateUp: () -> Unit = { }, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.supernova_logo_star_color_v2),
                    contentDescription = stringResource(Res.string.logo_description),
                    modifier = Modifier.size(40.dp).padding(end = MaterialTheme.spacing.small)
                )
                Text(
                    text = stringResource(Res.string.app_name),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_action_icon_description)
                    )
                }
            }
        }
    )
}
