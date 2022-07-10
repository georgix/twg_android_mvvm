package nz.co.warehouseandroidtest.ui.viewmodel

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
import nz.co.warehouseandroidtest.data.repository.UserRepository
import nz.co.warehouseandroidtest.domain.ListUseCase
import nz.co.warehouseandroidtest.domain.ListUseCaseImpl
import nz.co.warehouseandroidtest.domain.UserUseCase
import nz.co.warehouseandroidtest.domain.UserUseCaseImpl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ListViewModelTest {
    private lateinit var viewModel: ListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var useCase: ListUseCase

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ListViewModel(useCase)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            val data = Result.success(User("Prod", "1B2631A6-A5B7-47FE-840F-692D53E03724"))
            Mockito.`when`(useCase.getUserId()).thenReturn(MutableLiveData(data))
            viewModel.getUserId()
            val result = viewModel.user.getOrAwaitValue()
            Truth.assertThat(result).isNotNull()
            Truth.assertThat(result!!.isSuccess).isTrue()
            val user = (result as Result.Success<User>).data
            Truth.assertThat(user.UserID).isEqualTo("1B2631A6-A5B7-47FE-840F-692D53E03724")
        }

    }

    @Test
    fun getSearchResultTest() {
        runBlocking {
            val userData = Result.success(User("Prod", "1B2631A6-A5B7-47FE-840F-692D53E03724"))
            Mockito.`when`(useCase.getUserId()).thenReturn(MutableLiveData(userData))
            val searchData = Result.success(
                SearchResult("0",
                    emptyList(),
                    "Heat",
                    "PROD",
                    "NO"
                )
            )
            Mockito.`when`(useCase.getSearchProducts(emptyMap())).thenReturn(MutableLiveData(searchData))

            viewModel.getSearchProducts(emptyMap())
            val result = viewModel.products.getOrAwaitValue()
            Truth.assertThat(result).isNotNull()
            Truth.assertThat(result!!.isSuccess).isTrue()
            val searchResult = (result as Result.Success<SearchResult>).data
            Truth.assertThat(searchResult.HitCount).isEqualTo("0")
        }
    }
}