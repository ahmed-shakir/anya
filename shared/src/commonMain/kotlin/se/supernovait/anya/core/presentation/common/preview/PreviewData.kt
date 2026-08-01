package se.supernovait.anya.core.presentation.common.preview

import se.supernovait.anya.app.domain.model.MedicalRecordType
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.toLocalDate

object PreviewData {
    val address = AddressState(
        street = "Imaginary Street 3",
        city = "Dubai",
        county = "Dubai",
        country = "UAE"
    )

    val owner = OwnerState(
        id = 1,
        firstname = "Jane",
        lastname = "Doe",
        dob = "1990-07-20".toLocalDate(),
        address = address
    )

    val cat = CatState(
        id = 1,
        name = "Miss Mango",
        nickname = "Missy",
        dob = "2026-01-05".toLocalDate(),
        breed = "Farm cat",
        address = address
    )

    val medicalRecord = MedicalRecordState(
        catId = 1,
        type = MedicalRecordType.INJURY,
        title = "Bone fracture",
        description = "Fell and broke left paw",
        date = "2026-05-28".toLocalDate()
    )
}
