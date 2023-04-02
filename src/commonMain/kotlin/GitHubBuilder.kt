import io.ktor.client.HttpClient

public class GitHubBuilder {
    public var token: String? = null
    public var client: HttpClient? = null

    public fun build(): GitHub = client?.let { GitHub(token, it) } ?: GitHub(token)
}
