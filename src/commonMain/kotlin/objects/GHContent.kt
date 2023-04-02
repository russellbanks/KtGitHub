package objects

import ApiException
import GHResult
import io.ktor.client.request.setBody
import io.ktor.util.decodeBase64Bytes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GHContent(
    var type: Type,
    var encoding: Encoding? = null,
    var size: Int,
    var name: String,
    var path: String,
    @SerialName("content") var encodedContent: String? = null,
    var sha: String,
    var url: String,
    var gitUrl: String?,
    var htmlUrl: String?,
    var downloadUrl: String?,
    @SerialName("_links") var links: Links,
    var target: String? = null
) : GitHubObject() {
    @Serializable
    enum class Encoding {
        @SerialName("base64") BASE64;

        override fun toString() = name.lowercase()
    }

    @Serializable
    enum class Type {
        @SerialName("dir") DIRECTORY { override fun toString() = name.lowercase().take(3) },
        @SerialName("file") FILE { override fun toString() = name.lowercase() };
    }

    @Serializable
    class Links(
        val git: String?,
        val html: String?,
        val self: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Links

            if (git != other.git) return false
            if (html != other.html) return false
            return self == other.self
        }

        override fun hashCode(): Int {
            var result = git?.hashCode() ?: 0
            result = 31 * result + (html?.hashCode() ?: 0)
            result = 31 * result + self.hashCode()
            return result
        }

        override fun toString(): String {
            return "Links(git=$git, html=$html, self='$self')"
        }
    }

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
    suspend fun fetchDecodedContent(): GHResult<ByteArray> = if (encodedContent == null) {
        fetchFullContentData().map { it.encodedContent as String }
    } else {
        GHResult(Result.success(encodedContent as String))
    }.map(String::decodeBase64Bytes)

    suspend fun fetchDecodedString(): GHResult<String> = fetchDecodedContent().map(ByteArray::decodeToString)
    
    suspend fun fetchFullContentData(): GHResult<GHContent> = getWithConfig<GHContent>(url).onSuccess {
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
    suspend fun delete(commitMessage: String): GHResult<Unit> {
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
