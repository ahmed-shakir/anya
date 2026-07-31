package se.supernovait.anya.core.domain.sharing

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalEncodingApi::class)
class ShareUrlBuilderTest {

    @Test
    fun `test build url`() {
        val type = "cat"
        val data = "serialized_data"
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        val expected = "anya://share?type=cat&data=$encodedData"

        val actual = ShareUrlBuilder.build(type, data)

        assertEquals(expected, actual)
    }

    @Test
    fun `test build HTTPS url`() {
        val type = "cat"
        val data = "serialized_data"
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        val expected = "https://anya.supernovait.se/share?type=cat&data=$encodedData"

        val actual = ShareUrlBuilder.buildHttps(type, data)

        assertEquals(expected, actual)
    }

    @Test
    fun `test build url with special characters in data`() {
        val type = "cat"
        val data = "{\"id\":1,\"name\":\"Whiskers\"}"
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        val expected = "https://anya.supernovait.se/share?type=cat&data=$encodedData"
        
        val actual = ShareUrlBuilder.buildHttps(type, data)
        
        assertEquals(expected, actual)
    }

    @Test
    fun `test parse valid custom url`() {
        val type = "owner"
        val data = "more_data"
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        val url = "anya://share?type=$type&data=$encodedData"
        
        val (parsedType, parsedData) = ShareUrlBuilder.parse(url)
        
        assertEquals(type, parsedType)
        assertEquals(data, parsedData)
    }

    @Test
    fun `test parse valid https url`() {
        val type = "cat"
        val data = "some_data"
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        val url = "https://anya.supernovait.se/share?type=$type&data=$encodedData"
        
        val (parsedType, parsedData) = ShareUrlBuilder.parse(url)
        
        assertEquals(type, parsedType)
        assertEquals(data, parsedData)
    }

    @Test
    fun `test parse invalid scheme`() {
        val url = "wrong://share?type=cat&data=ZGF0YQ=="
        
        val (type, data) = ShareUrlBuilder.parse(url)
        
        assertNull(type)
        assertNull(data)
    }

    @Test
    fun `test parse invalid host`() {
        val url = "anya://wrong?type=cat&data=ZGF0YQ=="
        
        val (type, data) = ShareUrlBuilder.parse(url)
        
        assertNull(type)
        assertNull(data)
    }

    @Test
    fun `test parse malformed params`() {
        val url = "anya://share?wrong=cat&data=ZGF0YQ=="
        
        val (type, data) = ShareUrlBuilder.parse(url)
        
        assertNull(type)
        assertEquals("data", data)
    }
}
