package nz.co.warehouseandroidtest.data.remote

import nz.co.warehouseandroidtest.data.model.ProductDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PriceApi {
    @GET("bolt/price.json")
    suspend fun getProductDetail(@QueryMap paramMap: Map<String, String?>): Response<ProductDetail>
}