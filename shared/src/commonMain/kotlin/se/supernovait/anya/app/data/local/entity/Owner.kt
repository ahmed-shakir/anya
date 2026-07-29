package se.supernovait.anya.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.supernovait.anya.app.data.local.entity.embedded.Address

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
