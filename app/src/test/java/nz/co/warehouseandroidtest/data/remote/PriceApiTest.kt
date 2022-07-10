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

class PriceApiTest {
    private lateinit var service: PriceApi
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
            .create(PriceApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getProductDetailTest() = runBlocking {
        enqueueResponse("api-response/price.json", 200)
        val productDetail = service.getProductDetail(emptyMap()).body()

        assertThat(productDetail).isNotNull()
        assertThat(productDetail!!.ScanBarcode).isEqualTo("9400048541229")
        assertThat(productDetail!!.Product.Price.Price).isEqualTo("35")
    }

    private fun enqueueResponse(localFile: String, code: Int) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(localFile)
        val source = inputStream?.source()?.buffer()
        source?.let {
            mockWebServer.enqueue(MockResponse().setBody(it.readString(Charsets.UTF_8)).setResponseCode(code))
        }
    }

}