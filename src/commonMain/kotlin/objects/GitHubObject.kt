package objects

import ApiException
import GHJson
import GHResult
import GitHub
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class GitHubObject : KoinComponent {
    open val root: GitHub by inject()

    private val githubApiVersion = LocalDate(2022, 11, 28)

    internal suspend inline fun <reified T> getWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.get(urlString) {
        accept(GHJson)
        root.token?.let(::bearerAuth)
        header(githubApiVersionHeader, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> postWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.post(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(githubApiVersionHeader, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> patchWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.patch(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(githubApiVersionHeader, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> deleteWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.delete(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(githubApiVersionHeader, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> HttpResponse.handleResponse(): GHResult<T> = GHResult(
        when {
            status.isSuccess() -> Result.success(body())
            else -> Result.failure(ApiException(status, bodyAsText()))
        }
    )

    companion object {
        private const val githubApiVersionHeader = "X-GitHub-Api-Version"
    }
}
