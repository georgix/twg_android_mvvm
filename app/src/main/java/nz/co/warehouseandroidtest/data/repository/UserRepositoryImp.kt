package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.Single
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.common.RepositoryOps
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.remote.UserApi
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(val userApi: UserApi) : UserRepository{
    override fun getUserId(): LiveData<Result<User?>> = object: RepositoryOps<User>() {
        override suspend fun remoteDataSource(): Response<User> = userApi.getUserId()
    }.asLiveData()
}