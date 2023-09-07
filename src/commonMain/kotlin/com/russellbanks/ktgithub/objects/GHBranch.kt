package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHBranch internal constructor(
    public val name: String,
    public val commit: GHCommit,
    @SerialName("_links") public val links: Links,
    @SerialName("protected") public val isProtected: Boolean,
    // val protection: Protection
) {
    @Serializable
    @Poko public class Links(
        public val html: String,
        public val self: String
    )

    public val sha1: String get() = commit.sha
}
