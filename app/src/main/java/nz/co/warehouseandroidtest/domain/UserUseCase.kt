package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.User

interface UserUseCase {
    fun getUserId(): LiveData<Result<User?>>
}