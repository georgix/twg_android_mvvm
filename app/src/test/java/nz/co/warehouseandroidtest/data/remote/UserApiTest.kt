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

class UserApiTest {
    private lateinit var service: UserApi
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
            .create(UserApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUserTest() = runBlocking {
        enqueueResponse("api-response/newuser.json", 200)
        val user = service.getUserId().body()

        assertThat(user).isNotNull()
        assertThat(user!!.UserID).isEqualTo("1B2631A6-A5B7-47FE-840F-692D53E03724")
    }

    private fun enqueueResponse(localFile: String, code: Int) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(localFile)
        val source = inputStream?.source()?.buffer()
        source?.let {
            mockWebServer.enqueue(MockResponse().setBody(it.readString(Charsets.UTF_8)).setResponseCode(code))
        }
    }

}