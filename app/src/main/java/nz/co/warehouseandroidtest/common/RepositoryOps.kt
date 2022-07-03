package nz.co.warehouseandroidtest.common

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import retrofit2.Response

abstract class RepositoryOps<TResult> {
    private val TAG = RepositoryOps::class.java.simpleName

    fun asLiveData() : LiveData<Result<TResult?>> = liveData {
        localDataSource()?.let { emit(Result.success(it)) } ?: emit(Result.loading())

        val response = remoteDataSource()
        if (response.isSuccessful) {
            emit(Result.success(response.body()))
        } else {
            emit(Result.error(response.message()))
        }
    }

    @MainThread
    protected fun localDataSource(): TResult? = null

    @MainThread
    protected abstract suspend fun remoteDataSource(): Response<TResult>

    @WorkerThread
    protected suspend fun persistData(data: TResult) {
    }
}