package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.ApiException
import com.russellbanks.ktgithub.GHResult
import dev.drewhamilton.poko.Poko
import io.ktor.client.request.setBody
import io.ktor.util.decodeBase64Bytes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHContent(
    public var type: Type,
    public var encoding: Encoding? = null,
    public var size: Int,
    public var name: String,
    public var path: String,
    @SerialName("content") public var encodedContent: String? = null,
    public var sha: String,
    public var url: String,
    public var gitUrl: String?,
    public var htmlUrl: String?,
    public var downloadUrl: String?,
    @SerialName("_links") public var links: Links,
    public var target: String? = null
) : GitHubObject() {
    @Serializable
    public enum class Encoding {
        @SerialName("base64") BASE64;

        override fun toString(): String = name.lowercase()
    }

    @Serializable
    public enum class Type {
        @SerialName("dir") DIRECTORY { override fun toString(): String = name.lowercase().take(3) },
        @SerialName("file") FILE { override fun toString(): String = name.lowercase() };
    }

    @Serializable
    @Poko public class Links(
        public val git: String?,
        public val html: String?,
        public val self: String
    )

    /**
     * Fetches and decodes the content of a file from the GitHub API.
     *
     * If the initial GitHub API call was made to fetch a directory's content, it won't include the content of individual
     * files. In such cases, this function will make an additional API call to obtain the file content before decoding it.
     * If the content is already available, it will be decoded directly.
     *
     * @return The decoded content of the file as a ByteArray.
     * @throws ClassCastException If the decoded content is not a ByteArray.
     */
    public suspend fun fetchDecodedContent(): GHResult<ByteArray> = if (encodedContent == null) {
        fetchFullContentData().map { it.encodedContent as String }
    } else {
        GHResult(Result.success(encodedContent as String))
    }.map(String::decodeBase64Bytes)

    public suspend fun fetchDecodedString(): GHResult<String> = fetchDecodedContent().map(ByteArray::decodeToString)
    
    public suspend fun fetchFullContentData(): GHResult<GHContent> = getWithConfig<GHContent>(url).onSuccess {
        type = it.type
        encoding = it.encoding
        size = it.size
        name = it.name
        path = it.path
        encodedContent = it.encodedContent
        sha = it.sha
        url = it.url
        gitUrl = it.gitUrl
        htmlUrl = it.htmlUrl
        downloadUrl = it.downloadUrl
        links = it.links
        target = it.target
    }

    /**
     * Deletes a file in a repository.
     *
     * See [Contents#delete-a-file](https://docs.github.com/rest/repos/contents#delete-a-file)
     *
     * @param commitMessage The commit message for the file deletion.
     * @return A [GHResult] indicating the success or failure of the file deletion. Possible [ApiException] statuses
     * include:
     * - [HttpStatusCode.NotFound]: The file does not exist or the resource is not found.
     * - [HttpStatusCode.Conflict]: The request conflicts with another request (such as a parallel delete or update).
     * - [HttpStatusCode.UnprocessableEntity]: Validation failed or the endpoint has been spammed.
     * - [HttpStatusCode.ServiceUnavailable]: The service is temporarily unavailable.
     */
    public suspend fun delete(commitMessage: String): GHResult<Unit> {
        @Serializable
        class DeleteFileBody(
            val message: String,
            val sha: String
        )
        return deleteWithConfig(url) {
            setBody(DeleteFileBody(commitMessage, sha))
        }
    }
}
