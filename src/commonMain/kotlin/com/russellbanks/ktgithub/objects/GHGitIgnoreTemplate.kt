package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHGitIgnoreTemplate internal constructor(
    public val name: String,
    public val source: String
)
