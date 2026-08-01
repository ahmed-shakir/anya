package se.supernovait.anya.app.domain.model

import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_crutch
import anya.shared.generated.resources.ic_heart_pulse
import anya.shared.generated.resources.ic_syringe
import anya.shared.generated.resources.ic_virus
import org.jetbrains.compose.resources.DrawableResource

enum class MedicalRecordType(val icon: DrawableResource) {
    DISEASE(Res.drawable.ic_virus),
    INJURY(Res.drawable.ic_crutch),
    OTHER(Res.drawable.ic_heart_pulse),
    VACCINATION(Res.drawable.ic_syringe);

    companion object {
        fun ordinalOf(ordinal: Int): MedicalRecordType {
            return entries.first { it.ordinal == ordinal }
        }
    }
}
