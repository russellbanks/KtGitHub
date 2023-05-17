import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory

public class GitHubBuilder {
    public var token: String? = null

    public fun build(engine: HttpClientEngine): GitHub = GitHub(token, engine)
}
