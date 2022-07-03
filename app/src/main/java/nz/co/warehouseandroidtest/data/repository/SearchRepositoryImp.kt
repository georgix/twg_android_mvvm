package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.Single
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.RepositoryOps
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.remote.SearchApi
import nz.co.warehouseandroidtest.data.remote.UserApi
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImp @Inject constructor(val searchApi: SearchApi) : SearchRepository{
    override fun getSearchProducts(query: Map<String, String?>): LiveData<Result<SearchResult?>> = object: RepositoryOps<SearchResult>() {
        override suspend fun remoteDataSource(): Response<SearchResult> = searchApi.getSearchResult(query)
    }.asLiveData()
}