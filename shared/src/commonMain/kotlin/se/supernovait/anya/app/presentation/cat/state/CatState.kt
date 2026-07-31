package se.supernovait.anya.app.presentation.cat.state

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.owner.state.OwnerState

@Stable
data class CatState(
    val id: Long = 0,
    val ownerId: Long? = null,
    val name: String,
    val nickname: String,
    val dob: LocalDate?,
    val breed: String,
    val eyeColor: String = "",
    val furColor: String = "",
    val sterilized: Boolean = false,
    val imageUri: String? = null,
    val pedigreeUri: String? = null,
    val address: AddressState? = null,
    val owner: OwnerState? = null
) {
    companion object {
        val empty = CatState(name = "", nickname = "", dob = null, breed = "")
    }
}

fun CatState.isValid(): Boolean {
    return this.name.isNotBlank() &&
            this.nickname.isNotBlank() &&
            this.dob != null &&
            this.breed.isNotBlank() &&
            this.eyeColor.isNotBlank() &&
            this.furColor.isNotBlank()
}
