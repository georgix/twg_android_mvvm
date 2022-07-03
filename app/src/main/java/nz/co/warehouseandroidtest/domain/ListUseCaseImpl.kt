package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.SearchResult
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.repository.SearchRepository
import nz.co.warehouseandroidtest.data.repository.UserRepository
import javax.inject.Inject

class ListUseCaseImpl @Inject
    constructor(private val userUseCase: UserUseCase,
        private val searchRepository: SearchRepository) : ListUseCase {
    override fun getUserId(): LiveData<Result<User?>> = userUseCase.getUserId()

    override fun getSearchProducts(query: Map<String, String>): LiveData<Result<SearchResult?>> {
        val user = getUserId().value
        val queries = query.toMutableMap()
        if (user is Result.Success) {
            queries["UserID"] = user.data?.UserID ?: ""
        }
        return searchRepository.getSearchProducts(query)
    }
}