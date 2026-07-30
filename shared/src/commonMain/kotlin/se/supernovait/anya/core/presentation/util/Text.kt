package se.supernovait.anya.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class Text(
    val text: String,
    val contentDescription: String? = null
)

fun String.toText() = Text(this)

fun Text?.orEmpty() = this ?: Text("")

fun Text.contentDescription() = this.contentDescription ?: this.text

@Composable
fun textResource(resource: StringResource, contentDescription: String? = null): Text {
    return Text(text = stringResource(resource), contentDescription = contentDescription)
}
