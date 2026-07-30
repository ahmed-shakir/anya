package se.supernovait.anya.app.presentation.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_logo_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.topbar.AppTopBar
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState

@Composable
fun AnyaApp() {
    val topBarState = remember { TopBarState() }

    LaunchedEffect(Unit) {
        topBarState.reset()
    }

    CompositionLocalProvider(
        LocalTopBarState provides topBarState
    ) {
        Scaffold(
            topBar = {
                AppTopBar(state = topBarState)
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                Image(
                    painter = painterResource(Res.drawable.app_icon),
                    contentDescription = stringResource(Res.string.app_logo_description),
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = MaterialTheme.spacing.x3Large)
                )
            }
        }
    }
}
