package com.russellbanks.ktgithub

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel

public class GitHubBuilder {
    public var token: String? = null
    public var logLevel: LogLevel = LogLevel.INFO
    public var logger: (String) -> Unit = {}

    public fun build(engine: HttpClientEngine): GitHub = GitHub(token, engine, logger, logLevel)
}
