package com.russellbanks.ktgithub

import io.ktor.http.HttpStatusCode

public class ApiException internal constructor(
    public val status: HttpStatusCode,
    override val message: String
) : Throwable()
