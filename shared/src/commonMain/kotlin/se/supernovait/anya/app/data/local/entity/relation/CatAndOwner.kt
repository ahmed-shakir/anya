package se.supernovait.anya.app.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner

data class CatAndOwner(
    @Embedded
    val cat: Cat,

    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id"
    )
    val owner: Owner?
)
