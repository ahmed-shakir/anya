package se.supernovait.anya.app.data.local.dao

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.app.util.createInMemoryDatabase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DaoTests : AnyaBaseTest() {
    private lateinit var db: CatDatabase
    private lateinit var catDao: CatDao

    @BeforeTest
    fun setup() {
        db = createInMemoryDatabase()
        catDao = db.catDao()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `insert and get cat`() = runTest {
        val cat = Cat(id = 1, name = "Whiskers", nickname = "Whisk", dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue", furColor = "White")
        catDao.upsert(cat)
        
        val cats = catDao.getAll().first()
        assertEquals(1, cats.size)
        assertEquals("Whiskers", cats[0].name)
    }
}
