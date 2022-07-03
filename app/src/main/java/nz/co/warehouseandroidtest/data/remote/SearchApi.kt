package nz.co.warehouseandroidtest.data.remote

import nz.co.warehouseandroidtest.data.model.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchApi {
    @GET("bolt/search.json")
    suspend fun getSearchResult(@QueryMap paramMap: Map<String, String?>): Response<SearchResult>
}