class GHResult<T> internal constructor(private val result: Result<T>) {
    val isSuccess: Boolean get() = result.isSuccess
    val isFailure: Boolean get() = result.isFailure

    fun onSuccess(action: (T) -> Unit): GHResult<T> {
        result.getOrNull()?.let(action)
        return this
    }

    fun onFailure(action: (ApiException) -> Unit): GHResult<T> {
        exceptionOrNull()?.let(action)
        return this
    }

    suspend fun <R> map(transform: suspend (T) -> R): GHResult<R> {
        return GHResult(result.mapCatching { transform(it) })
    }

    suspend fun <R> flatMap(transform: suspend (T) -> GHResult<R>): GHResult<R> {
        return if (isSuccess) transform(getOrNull()!!)
        else GHResult(Result.failure(exceptionOrNull()!!))
    }

    fun getOrNull(): T? = result.getOrNull()

    fun getOrThrow(): T = result.getOrElse { throw it as ApiException }

    fun exceptionOrNull(): ApiException? = result.exceptionOrNull() as? ApiException
}
