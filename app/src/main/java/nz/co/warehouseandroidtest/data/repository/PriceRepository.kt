package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.model.SearchResult

interface PriceRepository {
    fun getProductDetail(query: Map<String, String?>): LiveData<Result<ProductDetail?>>
}