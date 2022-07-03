package nz.co.warehouseandroidtest.data.remote

import nz.co.warehouseandroidtest.data.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("bolt/newuser.json")
    suspend fun getUserId(): Response<User>
}