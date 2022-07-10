package nz.co.warehouseandroidtest.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.getOrAwaitValue
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.remote.UserApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class UserRepositoryTest {
    lateinit var repository: UserRepository
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var apiService: UserApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = UserRepositoryImp(apiService)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            Mockito.`when`(apiService.getUserId()).thenReturn(Response.success(User("Prod", "1B2631A6-A5B7-47FE-840F-692D53E03724")))
            val result = repository.getUserId().getOrAwaitValue()
            assertThat(result).isNotNull()
            assertThat(result!!.isSuccess).isTrue()
            val user = (result as Result.Success<User>).data
            assertThat(user.UserID).isEqualTo("1B2631A6-A5B7-47FE-840F-692D53E03724")
        }

    }
}