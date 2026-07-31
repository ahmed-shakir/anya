package se.supernovait.anya.app.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.fakes.FakeCatDao
import se.supernovait.anya.app.data.local.fakes.FakeOwnerDao
import se.supernovait.anya.app.util.AnyaBaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CatRepositoryImplTest : AnyaBaseTest() {
    private lateinit var catDao: FakeCatDao
    private lateinit var ownerDao: FakeOwnerDao
    private lateinit var repository: CatRepositoryImpl

    @BeforeTest
    fun setup() {
        catDao = FakeCatDao()
        ownerDao = FakeOwnerDao()
        repository = CatRepositoryImpl(catDao, ownerDao)
    }

    @Test
    fun `upsertCat calls catDao upsert`() = runTest {
        val cat = Cat(id = 1, name = "Whiskers", nickname = "Whisk", dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue", furColor = "White")
        repository.upsertCat(cat)
        
        repository.getAllCats(null).test {
            val cats = awaitItem()
            assertEquals(1, cats.size)
            assertEquals("Whiskers", cats[0].name)
        }
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
