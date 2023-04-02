import io.ktor.http.HttpStatusCode
data class ApiException internal constructor(val status: HttpStatusCode, override val message: String) : Throwable()
