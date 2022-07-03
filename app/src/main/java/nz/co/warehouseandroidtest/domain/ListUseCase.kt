package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User

interface ListUseCase {
    fun getUserId(): LiveData<Result<User?>>
    fun getSearchProducts(query: Map<String, String>): LiveData<Result<SearchResult?>>
}