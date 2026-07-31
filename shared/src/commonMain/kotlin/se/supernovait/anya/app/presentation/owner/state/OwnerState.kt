package se.supernovait.anya.app.presentation.owner.state

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.presentation.address.AddressState

@Stable
data class OwnerState(
    val id: Long = 0,
    val firstname: String,
    val lastname: String,
    val username: String? = null,
    val dob: LocalDate?,
    val imageUri: String? = null,
    val address: AddressState? = null
) {
    val name: String
        get() = "$firstname $lastname"

    companion object {
        val empty = OwnerState(firstname = "", lastname = "", dob = null)
    }
}

fun OwnerState.isValid(): Boolean {
    return this.firstname.isNotBlank() &&
            this.lastname.isNotBlank() &&
            this.dob != null
}

fun OwnerState.isUserValid(): Boolean {
    return this.isValid() && !this.username.isNullOrBlank()
}
