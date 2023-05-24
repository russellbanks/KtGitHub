package com.russellbanks.ktgithub

import io.ktor.client.engine.HttpClientEngine

public class GitHubBuilder {
    public var token: String? = null

    public fun build(engine: HttpClientEngine): GitHub = GitHub(token, engine)
}
