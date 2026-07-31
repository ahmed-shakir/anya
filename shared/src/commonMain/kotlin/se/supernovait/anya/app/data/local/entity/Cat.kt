package se.supernovait.anya.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import se.supernovait.anya.app.data.local.entity.embedded.Address
import se.supernovait.anya.app.data.local.entity.embedded.filterBySearchQuery

@Entity(tableName = "cats",
    indices = [Index(value = ["ownerId"])],
    foreignKeys = [ForeignKey(
        entity = Owner::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onUpdate = ForeignKey.SET_DEFAULT,
        onDelete = ForeignKey.SET_DEFAULT
    )]
)
data class Cat(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: Long? = null,
    val name: String,
    val nickname: String,
    val dob: String,
    val breed: String,
    val eyeColor: String,
    val furColor: String,
    val sterilized: Boolean = false,
    val imageUri: String? = null,
    val pedigreeUri: String? = null,
    @Embedded
    val address: Address? = null
)

fun Cat.filterBySearchQuery(searchQuery: String): Boolean {
    return this.name.contains(searchQuery, true) ||
            this.nickname.contains(searchQuery, true) ||
            this.dob.contains(searchQuery, true) ||
            this.breed.contains(searchQuery, true) ||
            this.eyeColor.contains(searchQuery, true) ||
            this.furColor.contains(searchQuery, true) ||
            this.address?.filterBySearchQuery(searchQuery) ?: false
}
