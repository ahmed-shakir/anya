package se.supernovait.anya.app.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner

data class OwnerWithCats(
    @Embedded val owner: Owner,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val cats: List<Cat> = emptyList()
)
