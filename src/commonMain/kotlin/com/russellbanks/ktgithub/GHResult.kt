package com.russellbanks.ktgithub

public class GHResult<T> internal constructor(private val result: Result<T>) {
    public val isSuccess: Boolean get() = result.isSuccess
    public val isFailure: Boolean get() = result.isFailure

    public fun onSuccess(action: (T) -> Unit): GHResult<T> {
        result.getOrNull()?.let(action)
        return this
    }

    public fun onFailure(action: (ApiException) -> Unit): GHResult<T> {
        exceptionOrNull()?.let(action)
        return this
    }

    public suspend fun <R> map(transform: suspend (T) -> R): GHResult<R> {
        return GHResult(result.mapCatching { transform(it) })
    }

    public suspend fun <R> flatMap(transform: suspend (T) -> GHResult<R>): GHResult<R> {
        return if (isSuccess) transform(getOrNull()!!)
        else GHResult(Result.failure(exceptionOrNull()!!))
    }

    public fun getOrNull(): T? = result.getOrNull()

    public fun getOrThrow(): T = result.getOrElse { throw it as ApiException }

    public fun exceptionOrNull(): ApiException? = result.exceptionOrNull() as? ApiException
}
