package se.supernovait.anya.core.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import anya.shared.generated.resources.file_action_upload_label
import anya.shared.generated.resources.file_action_view_label
import anya.shared.generated.resources.ic_file
import anya.shared.generated.resources.ic_visibility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.domain.file.PdfViewer
import se.supernovait.anya.core.domain.file.SharedFile
import se.supernovait.anya.core.domain.file.rememberFileManager
import se.supernovait.anya.core.domain.handler.PermissionCallback
import se.supernovait.anya.core.domain.handler.createPermissionHandler
import se.supernovait.anya.core.domain.model.file.MimeType
import se.supernovait.anya.core.domain.model.permission.PermissionStatus
import se.supernovait.anya.core.domain.model.permission.PermissionType
import se.supernovait.anya.core.domain.util.currentTimeMilliseconds
import kotlin.time.Clock

@Composable
fun Document(
    expandMenu: Boolean,
    uri: String? = null,
    filename: String = "attachment_${Clock.currentTimeMilliseconds()}.pdf",
    onDismissRequest: () -> Unit = {},
    onDocumentSelected: (uri: String?) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val pdfViewer = koinInject<PdfViewer>()
    var documentFile by remember { mutableStateOf<SharedFile?>(null) }
    var launchFiles by remember { mutableStateOf(value = false) }
    val menuItemTextModifier = Modifier.padding(end = MaterialTheme.spacing.extraLarge)
    val viewFileActionLabel = stringResource(Res.string.file_action_view_label)
    val uploadFileActionLabel = stringResource(Res.string.file_action_upload_label)
    val a11yButtonText = stringResource(Res.string.a11y_button)
    val a11yViewFileText = "$viewFileActionLabel $a11yButtonText"
    val a11yUploadFileText = "$uploadFileActionLabel $a11yButtonText"

    val permissionsManager = createPermissionHandler(object : PermissionCallback {
        override fun onPermissionStatus(permissionType: PermissionType, status: PermissionStatus) {
            when(status) {
                PermissionStatus.GRANTED -> {
                    when(permissionType) {
                        PermissionType.FILES -> launchFiles = true
                        else -> { }
                    }
                }
                else -> { }
            }
        }
    })

    val documentManager = rememberFileManager(filename = filename) { file ->
        coroutineScope.launch {
            withContext(Dispatchers.Default) {
                onDocumentSelected(file?.uri())
                documentFile = file
            }
        }
    }

    if(launchFiles) {
        if(permissionsManager.isPermissionGranted(PermissionType.FILES)) {
            documentManager.launch(listOf(MimeType.PDF))
        } else {
            permissionsManager.askPermission(PermissionType.FILES)
        }
        launchFiles = false
    }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expandMenu,
            shape = MaterialTheme.shapes.large,
            onDismissRequest = { onDismissRequest.invoke() }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.file_action_upload_label), modifier = menuItemTextModifier) },
                trailingIcon = { AnyaIcon(icon = Res.drawable.ic_file, size = 24.0.dp) },
                onClick = {
                    launchFiles = true
                    onDismissRequest.invoke()
                },
                modifier = Modifier.semantics { contentDescription = a11yUploadFileText }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.file_action_view_label), modifier = menuItemTextModifier) },
                trailingIcon = { AnyaIcon(icon = Res.drawable.ic_visibility, size = 24.0.dp) },
                enabled = !uri.isNullOrEmpty(),
                onClick = {
                    pdfViewer.openPDF(uri = uri!!)
                    onDismissRequest.invoke()
                },
                modifier = Modifier.semantics { contentDescription = a11yViewFileText }
            )
        }
    }
}
