package nz.co.warehouseandroidtest.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.getOrAwaitValue
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.remote.SearchApi
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
class SearchRepositoryTest {
    lateinit var repository: SearchRepository
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var apiService: SearchApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = SearchRepositoryImp(apiService)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            Mockito.`when`(apiService.getSearchResult(emptyMap())).thenReturn(Response.success(
                SearchResult("0",
                    emptyList(),
                    "Heat",
                    "PROD",
                    "NO"
                )
            ))
            val result = repository.getSearchProducts(emptyMap()).getOrAwaitValue()
            assertThat(result).isNotNull()
            assertThat(result!!.isSuccess).isTrue()
            val searchResult = (result as Result.Success<SearchResult>).data
            assertThat(searchResult.HitCount).isEqualTo("0")
        }

    }
}