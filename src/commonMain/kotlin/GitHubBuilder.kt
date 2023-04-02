import io.ktor.client.HttpClient

class GitHubBuilder {
    var token: String? = null
    var client: HttpClient? = null

    fun build(): GitHub = client?.let { GitHub(token, it) } ?: GitHub(token)
}
