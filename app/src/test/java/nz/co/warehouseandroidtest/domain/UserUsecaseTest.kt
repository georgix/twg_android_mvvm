package nz.co.warehouseandroidtest.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.getOrAwaitValue
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.repository.UserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserUsecaseTest {
    private lateinit var usecase: UserUseCase
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var repository: UserRepository

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        usecase = UserUseCaseImpl(repository)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            val data = Result.success(User("Prod", "1B2631A6-A5B7-47FE-840F-692D53E03724"))
            Mockito.`when`(repository.getUserId()).thenReturn(MutableLiveData(data))
            val result = usecase.getUserId().getOrAwaitValue()
            Truth.assertThat(result).isNotNull()
            Truth.assertThat(result!!.isSuccess).isTrue()
            val user = (result as Result.Success<User>).data
            Truth.assertThat(user.UserID).isEqualTo("1B2631A6-A5B7-47FE-840F-692D53E03724")
        }

    }
}