package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.clear_field_action_icon_description
import anya.composeapp.generated.resources.date_picker_action_icon_description
import anya.composeapp.generated.resources.hide_password_action_icon_description
import anya.composeapp.generated.resources.search_action_icon_description
import anya.composeapp.generated.resources.show_password_action_icon_description
import anya.composeapp.generated.resources.validation_date_format
import anya.composeapp.generated.resources.visibility
import anya.composeapp.generated.resources.visibility_off
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import se.supernovait.anya.core.domain.entity.validation.ValidationPattern
import se.supernovait.anya.core.domain.entity.validation.ValidationResult
import se.supernovait.anya.core.domain.entity.validation.ValidationRule
import se.supernovait.anya.core.domain.entity.validation.ValidationRules
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.AnyaIconButton

@Composable
fun AnyaTextField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules? = null,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    isMultiline: Boolean = false,
    maxChar: Int = 36
) {
    AnyaInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        state = state,
        validationRules = validationRules,
        onValueChange = onValueChange,
        isMultiline = isMultiline,
        maxChar = maxChar
    )
}

@Composable
fun AnyaPasswordField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules? = null,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    maxChar: Int = 36,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    AnyaInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        trailingIcon = {
            val icon = if (passwordVisible) Res.drawable.visibility else Res.drawable.visibility_off
            val descriptionId = if (passwordVisible) Res.string.hide_password_action_icon_description else Res.string.show_password_action_icon_description

            AnyaIconButton(icon = vectorResource(icon), descriptionId = descriptionId, onClick = { passwordVisible = !passwordVisible })
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        state = state,
        validationRules = validationRules,
        onValueChange = onValueChange,
        maxChar = maxChar,
        isMultiline = false,
        isPassword = true
    )
}

@Composable
fun AnyaSearchField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules? = null,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    onSearch: () -> Unit,
) {
    AnyaInputField(
        modifier = modifier,
        label = placeholder,
        trailingIcon = {
            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                AnyaIcon(
                    icon = Icons.Outlined.Clear,
                    descriptionId = Res.string.clear_field_action_icon_description,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.clickable(onClick = { state.value("") })
                )
                AnyaIconButton(
                    icon = Icons.Outlined.Search,
                    descriptionId = Res.string.search_action_icon_description,
                    onClick = onSearch
                )
            }
        },
        imeAction = ImeAction.Search,
        state = state,
        validationRules = validationRules,
        onValueChange = onValueChange,
        maxChar = 36
    )
}

@Composable
fun AnyaDateField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    datePickerState: DatePickerState,
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules = getDefaultDateValidationRules(),
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    onOpenPicker: () -> Unit
) {
    val maxChar = 10

    OutlinedTextField(
        value = datePickerState.date?.toString().orEmpty(),
        onValueChange = { text ->
            // This line will take (in case the user try to paste a text from the clipboard) only the allowed amount of characters
            val croppedText = text.take(maxChar).replace("\n", "")
            val validationResult = validate(validationRules, text)
            state.value(croppedText)
            state.error(validationResult.error)
            state.setIsValid(validationResult.isValid)
            onValueChange(state.value, state.isValid)
        },
        isError = !state.isValid,
        readOnly = true,
        singleLine = true,
        maxLines = 1,
        trailingIcon = {
            val icon = Icons.Outlined.DateRange
            val descriptionId = Res.string.date_picker_action_icon_description
            AnyaIconButton(icon = icon, descriptionId = descriptionId, onClick = onOpenPicker)
        },
        label = { Text(text = state.error ?: label) },
        placeholder = { Text(text = placeholder) },
        supportingText = { },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    )
}

@Composable
private fun AnyaInputField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Unspecified,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    state: InputFieldState = rememberInputFieldState(""),
    validationRules: ValidationRules? = null,
    onValueChange: (value: String, isValid: Boolean) -> Unit,
    isMultiline: Boolean = false,
    isPassword: Boolean = false,
    maxChar: Int = 36
) {
    val charsCount = "${state.value.length}/$maxChar"

    OutlinedTextField(
        value = state.value,
        onValueChange = { text ->
            // This line will take (in case the user try to paste a text from the clipboard) only the allowed amount of characters
            val croppedText = if (isMultiline) text.take(maxChar) else text.take(maxChar).replace("\n", "")
            val validationResult = validate(validationRules, text)
            state.value(croppedText)
            state.error(validationResult.error)
            state.setIsValid(validationResult.isValid)
            onValueChange(state.value, state.isValid)
        },
        isError = !state.isValid,
        singleLine = !isMultiline,
        maxLines = if (isMultiline) 5 else 1,
        label = { Text(text = state.error ?: label) },
        placeholder = { Text(text = placeholder) },
        supportingText = {
            if (isMultiline && maxChar != Int.MAX_VALUE) {
                Text(text = charsCount, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Unspecified,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
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

@Composable
private fun getDefaultDateValidationRules() = ValidationRules(regex = ValidationRule(value = ValidationPattern.ISO_DATE, message = stringResource(Res.string.validation_date_format)))
