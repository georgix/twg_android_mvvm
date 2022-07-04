package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.RepositoryOps
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.remote.PriceApi
import retrofit2.Response
import javax.inject.Inject

class PriceRepositoryImp @Inject constructor(val priceApi: PriceApi) : PriceRepository{
    override fun getProductDetail(query: Map<String, String?>): LiveData<Result<ProductDetail?>> = object: RepositoryOps<ProductDetail>() {
        override suspend fun remoteDataSource(): Response<ProductDetail> = priceApi.getProductDetail(query)
    }.asLiveData()
}