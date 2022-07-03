package nz.co.warehouseandroidtest.data.repository

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.common.Result

interface UserRepository {
    fun getUserId(): LiveData<Result<User?>>
}