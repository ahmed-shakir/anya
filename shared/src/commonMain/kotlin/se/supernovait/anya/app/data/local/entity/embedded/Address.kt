package se.supernovait.anya.app.data.local.entity.embedded

import androidx.room.ColumnInfo

data class Address(
    val street: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String = "",
    val city: String,
    val county: String = "",
    val country: String,
)

fun Address.filterBySearchQuery(searchQuery: String): Boolean {
    return this.street.contains(searchQuery, true) ||
            this.postalCode.contains(searchQuery, true) ||
            this.city.contains(searchQuery, true) ||
            this.county.contains(searchQuery, true) ||
            this.country.contains(searchQuery, true)
}
