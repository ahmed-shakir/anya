package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_clear_field_content_description
import anya.shared.generated.resources.a11y_action_date_picker_content_description
import anya.shared.generated.resources.a11y_action_hide_password_content_description
import anya.shared.generated.resources.a11y_action_search_content_description
import anya.shared.generated.resources.a11y_action_show_password_content_description
import anya.shared.generated.resources.a11y_date_picker
import anya.shared.generated.resources.a11y_input_field
import anya.shared.generated.resources.a11y_password_field
import anya.shared.generated.resources.a11y_search_field
import anya.shared.generated.resources.a11y_state_disabled
import anya.shared.generated.resources.ic_close
import anya.shared.generated.resources.ic_date_range
import anya.shared.generated.resources.ic_search
import anya.shared.generated.resources.ic_visibility
import anya.shared.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.domain.model.validation.ValidationPattern
import se.supernovait.anya.core.domain.model.validation.ValidationResult
import se.supernovait.anya.core.domain.model.validation.ValidationRules
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.input_field.state.DateFieldState
import se.supernovait.anya.core.presentation.common.input_field.state.InputFieldState
import se.supernovait.anya.core.presentation.common.input_field.state.rememberDateFieldState
import se.supernovait.anya.core.presentation.common.input_field.state.rememberInputFieldState

@Composable
fun AnyaTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    initialValue: String = "",
    contentDescription: String? = null,
    state: InputFieldState = rememberInputFieldState(initialValue),
    validationRules: ValidationRules = ValidationRules.defaultRules,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    isMultiline: Boolean = false,
    fullWidth: Boolean = true,
    enabled: Boolean = true,
    maxChar: Int = 36
) {
    AnyaInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        contentDescription = contentDescription,
        state = state,
        validationRules = validationRules,
        onValueChange = onValueChange,
        isMultiline = isMultiline,
        maxChar = maxChar,
        fullWidth = fullWidth,
        enabled = enabled
    )
}

@Composable
fun AnyaPasswordField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    contentDescription: String? = null,
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules = ValidationRules.defaultRules,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    fullWidth: Boolean = true,
    enabled: Boolean = true,
    maxChar: Int = 36
) {
    val a11yPasswordFieldText = stringResource(Res.string.a11y_password_field)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    AnyaInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        contentDescription = contentDescription ?: a11yPasswordFieldText,
        trailingIcon = {
            val iconResource = if (passwordVisible) Res.drawable.ic_visibility else Res.drawable.ic_visibility_off
            val a11yResource = if (passwordVisible) Res.string.a11y_action_hide_password_content_description else Res.string.a11y_action_show_password_content_description

            AnyaIconButton(
                icon = iconResource,
                contentDescription = stringResource(a11yResource),
                onClick = { passwordVisible = !passwordVisible }
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        state = state,
        validationRules = validationRules,
        onValueChange = onValueChange,
        maxChar = maxChar,
        isMultiline = false,
        isPassword = true,
        fullWidth = fullWidth,
        enabled = enabled
    )
}

@Composable
fun AnyaSearchField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    contentDescription: String? = null,
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules = ValidationRules.defaultRules,
    onSearch: (query: String) -> Unit,
    fullWidth: Boolean = true,
    enabled: Boolean = true
) {
    val a11ySearchFieldText = stringResource(Res.string.a11y_search_field)

    AnyaInputField(
        label = placeholder,
        contentDescription = contentDescription ?: a11ySearchFieldText,
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnyaIcon(
                    icon = Res.drawable.ic_close,
                    contentDescription = Res.string.a11y_action_clear_field_content_description,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.clickable(onClick = {
                        state.value("")
                        onSearch(state.value)
                    })
                )
                AnyaIcon(
                    icon = Res.drawable.ic_search,
                    contentDescription = Res.string.a11y_action_search_content_description,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(onClick = { onSearch(state.value) })
                        .padding(end = MaterialTheme.spacing.extraSmall)
                )
            }
        },
        imeAction = ImeAction.Search,
        state = state,
        validationRules = validationRules,
        onValueChange = { _, _ ->  },
        fullWidth = fullWidth,
        enabled = enabled,
        maxChar = 36,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnyaDateField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    initialValue: String = "",
    contentDescription: String? = null,
    state: DateFieldState = rememberDateFieldState(date = initialValue.toLocalDate()),
    validationRules: ValidationRules = ValidationRules.defaultDateRules,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    fullWidth: Boolean = true,
    enabled: Boolean = true
) {
    val a11yDatePickerText = stringResource(Res.string.a11y_date_picker)
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val datePickerState: DatePickerState = rememberDatePickerState()
    val fullWidthModifier = Modifier.fillMaxWidth().takeIf { fullWidth } ?: Modifier
    val fieldModifier = fullWidthModifier
        .padding(horizontal = MaterialTheme.spacing.extraSmall)
        .then(modifier)
        .pointerInput(state.date) {
            awaitEachGesture {
                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput in
                // the Initial pass to observe events before the text field consumes them in the Main pass.
                awaitFirstDown(pass = PointerEventPass.Initial)
                val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (upEvent != null) {
                    state.setIsPickerOpen(true)
                }
            }
        }
        .semantics {
            this.contentDescription = buildString {
                if (!contentDescription.isNullOrBlank()) {
                    append(contentDescription)
                } else if (label.isNotBlank()) {
                    append("$label $a11yDatePickerText")
                } else if (placeholder.isNotBlank()) {
                    append(placeholder)
                } else {
                    append(a11yDatePickerText)
                }
                if (!enabled) append(" $a11yStatusDisabled")
            }
        }

    LaunchedEffect(state) {
        snapshotFlow { state.date }
            .collect {
                onValueChange(state.date.isoString(), validate(validationRules, state.date.isoString()).isValid)
            }
    }

    OutlinedTextField(
        value = state.date.isoString(),
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        enabled = enabled,
        maxLines = 1,
        trailingIcon = {
            AnyaIcon(icon = Res.drawable.ic_date_range, contentDescription = Res.string.a11y_action_date_picker_content_description)
        },
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        modifier = fieldModifier
    )

    if (state.isPickerOpen) {
        AnyaDatePickerDialog(
            datePickerState = datePickerState,
            onDateSelected = { state.date(it) },
            onDismissRequest = { state.setIsPickerOpen(false) }
        )
    }
}

@Composable
private fun AnyaInputField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    contentDescription: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Unspecified,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules? = null,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    isMultiline: Boolean = false,
    isPassword: Boolean = false,
    fullWidth: Boolean = true,
    enabled: Boolean = true,
    maxChar: Int = 36
) {
    val charsCount = "${state.value.length}/$maxChar"
    val a11yInputFieldText = stringResource(Res.string.a11y_input_field)
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val fullWidthModifier = Modifier.fillMaxWidth().takeIf { fullWidth } ?: Modifier
    val fieldModifier = fullWidthModifier
        .padding(horizontal = MaterialTheme.spacing.extraSmall)
        .then(modifier)
        .semantics {
            this.contentDescription = buildString {
                if (!contentDescription.isNullOrBlank()) {
                    append(contentDescription)
                } else if (label.isNotBlank()) {
                    append("$label $a11yInputFieldText")
                } else if (placeholder.isNotBlank()) {
                    append(placeholder)
                } else {
                    append(a11yInputFieldText)
                }
                if (!enabled) append(" $a11yStatusDisabled")
            }
        }

    OutlinedTextField(
        value = state.value,
        onValueChange = { text ->
            // This line will take (in case the user try to paste a text from the clipboard) only the allowed amount of characters
            val croppedText = if (isMultiline) text.take(maxChar) else text.take(maxChar).replace("\n", "")
            val validationResult = validate(validationRules, croppedText)
            state.value(croppedText)
            state.error(validationResult.error)
            state.setIsValid(validationResult.isValid)
            onValueChange(state.value, state.isValid)
        },
        isError = !state.isValid,
        enabled = enabled,
        singleLine = !isMultiline,
        maxLines = if (isMultiline) 5 else 1,
        label = { Text(text = state.error ?: label) },
        placeholder = { Text(text = placeholder) },
        supportingText = if (isMultiline && maxChar != Int.MAX_VALUE) {
            { Text(text = charsCount, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) }
        } else null,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Unspecified,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        modifier = fieldModifier
    )
}

private fun validate(validationRules: ValidationRules? = null, text: String): ValidationResult {
    if (validationRules != null) {
        validationRules.minLength?.let {
            if (text.length < it.value) {
                return ValidationResult(it.message)
            }
        }
        validationRules.maxLength?.let {
            if (text.length > it.value) {
                return ValidationResult(it.message)
            }
        }
        validationRules.regex?.let {
            val regex = it.value
            val pattern = if (regex == ValidationPattern.CUSTOM) it.customValue else regex.pattern
            if (pattern != null && !text.matches(pattern.toRegex())) {
                return ValidationResult(it.message)
            }
        }
    }
    return ValidationResult()
}
