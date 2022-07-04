package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User

interface DetailUseCase {
    fun getProductDetail(query: Map<String, String>): LiveData<Result<ProductDetail?>>
}