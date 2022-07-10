package nz.co.warehouseandroidtest.data.remote

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchApiTest {
    private lateinit var service: SearchApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create(
                GsonBuilder().create())
            )
            .build()
            .create(SearchApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getSearchResultTest() = runBlocking {
        enqueueResponse("api-response/search.json", 200)
        val searchResult = service.getSearchResult(emptyMap()).body()

        assertThat(searchResult).isNotNull()

        val products = searchResult!!.Results
        assertThat(products.size).isEqualTo(19)
        assertThat(products[0]!!.Products[0]!!.Barcode).isEqualTo("9400048541229")
    }

    private fun enqueueResponse(localFile: String, code: Int) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(localFile)
        val source = inputStream?.source()?.buffer()
        source?.let {
            mockWebServer.enqueue(MockResponse().setBody(it.readString(Charsets.UTF_8)).setResponseCode(code))
        }
    }

}