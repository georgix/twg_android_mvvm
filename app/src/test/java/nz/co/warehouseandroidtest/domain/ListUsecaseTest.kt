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
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.repository.SearchRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ListUsecaseTest {
    private lateinit var usecase: ListUseCase
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var searchRepository: SearchRepository

    @Mock
    lateinit var userUseCase: UserUseCase

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        usecase = ListUseCaseImpl(userUseCase, searchRepository)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            val userData = Result.success(User("Prod", "1B2631A6-A5B7-47FE-840F-692D53E03724"))
            Mockito.`when`(userUseCase.getUserId()).thenReturn(MutableLiveData(userData))
            val searchData = Result.success(
                SearchResult("0",
                    emptyList(),
                    "Heat",
                    "PROD",
                    "NO"
                )
            )
            Mockito.`when`(searchRepository.getSearchProducts(emptyMap())).thenReturn(MutableLiveData(searchData))
            val result = usecase.getSearchProducts(emptyMap()).getOrAwaitValue()
            Truth.assertThat(result).isNotNull()
            Truth.assertThat(result!!.isSuccess).isTrue()
            val searchResult = (result as Result.Success<SearchResult>).data
            Truth.assertThat(searchResult.HitCount).isEqualTo("0")
        }

    }
}