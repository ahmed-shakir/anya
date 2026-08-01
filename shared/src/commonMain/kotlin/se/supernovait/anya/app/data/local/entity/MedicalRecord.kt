package se.supernovait.anya.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import se.supernovait.anya.app.domain.model.MedicalRecordType

@Entity(tableName = "medical_records", indices = [Index(value = ["catId"])])
data class MedicalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val catId: Long,
    val type: MedicalRecordType,
    val title: String,
    val description: String,
    val date: String,
    val contagious: Boolean = false
)

fun MedicalRecord.filterBySearchQuery(searchQuery: String): Boolean {
    return this.type.name.contains(searchQuery, true) ||
            this.title.contains(searchQuery, true) ||
            this.description.contains(searchQuery, true) ||
            this.date.contains(searchQuery, true)
}
