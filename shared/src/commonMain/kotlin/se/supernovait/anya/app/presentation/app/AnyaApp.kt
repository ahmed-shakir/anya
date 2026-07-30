package se.supernovait.anya.app.presentation.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_logo_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing

@Composable
fun AnyaApp() {
    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
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
