package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.ApiException
import com.russellbanks.ktgithub.GHJson
import com.russellbanks.ktgithub.GHResult
import com.russellbanks.ktgithub.GitHub
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

public abstract class GitHubObject {
    public lateinit var root: GitHub
    private val githubApiVersion = LocalDate(2022, 11, 28)

    internal suspend inline fun <reified T> getWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.get(urlString) {
        accept(GHJson)
        root.token?.let(::bearerAuth)
        header(GITHUB_API_VERSION_HEADER, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> postWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.post(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(GITHUB_API_VERSION_HEADER, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> patchWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.patch(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(GITHUB_API_VERSION_HEADER, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> deleteWithConfig(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): GHResult<T> = root.client.delete(urlString) {
        accept(GHJson)
        contentType(ContentType.Application.Json)
        root.token?.let(::bearerAuth)
        header(GITHUB_API_VERSION_HEADER, githubApiVersion)
        block()
    }.handleResponse()

    internal suspend inline fun <reified T> HttpResponse.handleResponse(): GHResult<T> {
        return GHResult(
            when {
                status.isSuccess() -> Result.success(body<T>().also { if (it is GitHubObject) it.root = root })
                else -> Result.failure(ApiException(status, bodyAsText()))
            }
        )
    }

    public companion object {
        private const val GITHUB_API_VERSION_HEADER = "X-GitHub-Api-Version"
    }
}
