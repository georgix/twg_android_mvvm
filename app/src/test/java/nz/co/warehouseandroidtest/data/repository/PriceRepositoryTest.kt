package nz.co.warehouseandroidtest.data.repository

import android.icu.lang.UCharacter
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.getOrAwaitValue
import nz.co.warehouseandroidtest.data.model.Price
import nz.co.warehouseandroidtest.data.model.Product
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.remote.PriceApi
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
class PriceRepositoryTest {
    lateinit var repository: PriceRepository
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var apiService: PriceApi

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = PriceRepositoryImp(apiService)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            Mockito.`when`(apiService.getProductDetail(emptyMap())).thenReturn(Response.success(
                ProductDetail(  MachineID = "123456789",
                Action = "S", ScanBarcode = "9400048541229",
            ScanID = "1868332",
            UserDescription= "",
            Product= Product(
            Class0 = "Leisure",
            Price= Price(
            Price = "35",
            Type ="NAT"
            ),
            Barcode ="9400048541229",
            ItemDescription = "",
            DeptID= "06022",
            SubClass= "Heat Guns",
            Class0ID= "09939",
            SubDeptID= "6240",
            Description= "Mako Heat Gun",
            BranchPrice= "35",
            ItemCode= "",
            SubDept= "Power Tools",
            ClassID="6805",
            ImageURL="https://twg.azure-api.net/twlProductImage/productImage/9400048541229/format/png/size/small",
            Dept= "Hardware",
            SubClassID= "4770",
            Class= "Power Tools",
            ProductKey = "1882523"
            ),
            ProdQAT= "Prod",
            ScanDateTime= "2022-07-10T23:21:32",
            Found= "Y", Branch = "208",
            UserID="1B2631A6-A5B7-47FE-840F-692D53E03724")
            ))
            val result = repository.getProductDetail(emptyMap()).getOrAwaitValue()
            assertThat(result).isNotNull()
            assertThat(result!!.isSuccess).isTrue()
            val priceDetail = (result as Result.Success<ProductDetail>).data
            assertThat(priceDetail.ScanBarcode).isEqualTo("9400048541229")
        }

    }
}