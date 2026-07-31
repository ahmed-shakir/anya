package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.data.local.entity.embedded.Address
import kotlin.test.Test
import kotlin.test.assertEquals

class DtoMapperTest {

    @Test
    fun `test address mapping`() {
        val entity = Address(
            street = "Main St",
            postalCode = "12345",
            city = "City",
            county = "County",
            country = "Country"
        )
        val dto = entity.mapToDto()
        val mappedBack = dto.mapToEntity()

        assertEquals(entity, mappedBack)
    }

    @Test
    fun `test cat mapping`() {
        val entity = Cat(
            id = 1,
            name = "Whiskers",
            nickname = "Whisk",
            dob = "2020-01-01",
            breed = "Siamese",
            eyeColor = "Blue",
            furColor = "White",
            address = Address("Street", "123", "City", "County", "Country")
        )
        val dto = entity.mapToDto()
        val mappedBack = dto.mapToEntity()

        assertEquals(entity, mappedBack)
    }

    @Test
    fun `test owner mapping`() {
        val entity = Owner(
            id = 1,
            firstname = "John",
            lastname = "Doe",
            username = "johndoe",
            dob = "1990-01-01",
            address = Address("Street", "123", "City", "County", "Country")
        )
        val dto = entity.mapToDto()
        val mappedBack = dto.mapToEntity()
        
        assertEquals(entity, mappedBack)
    }
}
