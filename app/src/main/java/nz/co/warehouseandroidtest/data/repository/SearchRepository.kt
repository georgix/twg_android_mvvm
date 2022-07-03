package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.SearchResult

interface SearchRepository {
    fun getSearchProducts(query: Map<String, String?>): LiveData<Result<SearchResult?>>
}