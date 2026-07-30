package se.supernovait.anya.app.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import se.supernovait.anya.app.data.local.fakes.FakeOwnerDao
import se.supernovait.anya.app.util.AnyaBaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CatRepositoryImplTest : AnyaBaseTest() {
    private lateinit var ownerDao: FakeOwnerDao
    private lateinit var repository: CatRepositoryImpl

    @BeforeTest
    fun setup() {
        ownerDao = FakeOwnerDao()
        repository = CatRepositoryImpl(ownerDao)
    }

    @Test
    fun `upsertOwner calls ownerDao upsert`() = runTest {
        val owner = se.supernovait.anya.app.data.local.entity.Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01")
        repository.upsertOwner(owner)
        
        repository.getAllOwners().test {
            val owners = awaitItem()
            assertEquals(1, owners.size)
            assertEquals("John", owners[0].firstname)
        }
    }
}
