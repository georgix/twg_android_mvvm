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
import nz.co.warehouseandroidtest.data.model.Price
import nz.co.warehouseandroidtest.data.model.Product
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.repository.PriceRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailUsecaseTest {
    private lateinit var usecase: DetailUseCase
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var repository: PriceRepository

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        usecase = DetailUseCaseImpl(repository)
    }

    @Test
    fun getUserTest() {
        runBlocking {
            val data = Result.success(
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
            )
            Mockito.`when`(repository.getProductDetail(emptyMap())).thenReturn(MutableLiveData(data))
            val result = usecase.getProductDetail(emptyMap()).getOrAwaitValue()
            Truth.assertThat(result).isNotNull()
            Truth.assertThat(result!!.isSuccess).isTrue()
            val productDetail = (result as Result.Success<ProductDetail>).data
            Truth.assertThat(productDetail.ScanBarcode).isEqualTo("9400048541229")
        }

    }
}