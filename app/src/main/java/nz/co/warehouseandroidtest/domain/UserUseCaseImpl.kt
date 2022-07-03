package nz.co.warehouseandroidtest.domain

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.User
import nz.co.warehouseandroidtest.data.repository.UserRepository
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : UserUseCase {
    override fun getUserId(): LiveData<Result<User?>> = userRepository.getUserId()
}