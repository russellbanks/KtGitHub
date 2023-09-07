package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHGist(
    public val url: String,
    public val forksUrl: String,
    public val commitsUrl: String,
    public val id: String,
    public val gitPullUrl: String,
    public val gitPushUrl: String,
    public val htmlUrl: String
)
