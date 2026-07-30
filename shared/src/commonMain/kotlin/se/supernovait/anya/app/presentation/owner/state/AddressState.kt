package se.supernovait.anya.app.presentation.owner.state

data class AddressState(
    val id: Long = 0,
    val street: String,
    val postalCode: String = "",
    val city: String,
    val county: String,
    val country: String
) {
    companion object {
        val empty = AddressState(street = "", city = "", county = "", country = "")
    }
}

fun AddressState.isValid(): Boolean {
    return this.street.isNotBlank() &&
            this.city.isNotBlank() &&
            this.county.isNotBlank() &&
            this.country.isNotBlank()
}
