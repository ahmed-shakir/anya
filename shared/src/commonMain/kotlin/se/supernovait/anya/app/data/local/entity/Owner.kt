package se.supernovait.anya.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.supernovait.anya.app.data.local.entity.embedded.Address
import se.supernovait.anya.app.data.local.entity.embedded.filterBySearchQuery

@Entity(tableName = "owners")
data class Owner(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstname: String,
    val lastname: String,
    val username: String?,
    val dob: String,
    val imageUri: String? = null,
    @Embedded
    val address: Address? = null,
)

fun Owner.filterBySearchQuery(searchQuery: String): Boolean {
    return this.firstname.contains(searchQuery, true) ||
            this.lastname.contains(searchQuery, true) ||
            this.dob.contains(searchQuery, true) ||
            this.address?.filterBySearchQuery(searchQuery) ?: false
}
