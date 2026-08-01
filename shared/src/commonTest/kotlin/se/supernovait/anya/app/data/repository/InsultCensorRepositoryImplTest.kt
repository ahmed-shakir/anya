package se.supernovait.anya.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import se.supernovait.anya.app.data.remote.InsultCensorApi
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.app.util.createTestDataStore
import se.supernovait.anya.core.domain.util.Result
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class InsultCensorRepositoryImplTest : AnyaBaseTest() {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: InsultCensorRepositoryImpl

    @BeforeTest
    fun setup() {
        dataStore = createTestDataStore()
    }

    @Test
    fun `incrementCounter increments and returns value`() = runTest {
        // Mock API (not used in this test but needed for repository)
        val mockEngine = MockEngine { respond("") }
        val httpClient = HttpClient(mockEngine)
        val api = InsultCensorApi(httpClient)
        repository = InsultCensorRepositoryImpl(dataStore, api)

        assertEquals(0, repository.getCounter())
        
        val first = repository.incrementCounter()
        assertEquals(1, first)
        assertEquals(1, repository.getCounter())

        val second = repository.incrementCounter()
        assertEquals(2, second)
        assertEquals(2, repository.getCounter())
    }

    @Test
    fun `censorWords calls api and returns result`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "{\"result\": \"censored text\"}",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }
        val api = InsultCensorApi(httpClient)
        repository = InsultCensorRepositoryImpl(dataStore, api)

        val result = repository.censorWords("bad words")
        
        assertTrue(result is Result.Success)
        assertEquals("censored text", (result as Result.Success).data)
    }
}
