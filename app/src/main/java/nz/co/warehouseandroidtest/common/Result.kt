package nz.co.warehouseandroidtest.common

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val msg: String): Result<T>()
    object Loading: Result<Nothing>()

    val isSuccess get() = this is Success && data != null
    val isError get() = this is Error
    val isLoading get() = this is Loading

    companion object {
        fun <T> success(data: T) = Success(data)

        fun <T> error(msg: String) = Error<T>(msg)

        fun <T> loading(): Result<T> = Loading
    }
}
