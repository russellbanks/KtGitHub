package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHCommitResponse(
    public val sha: String,
    public val nodeId: String,
    public val url: String,
    public val author: GHCommit.GitUser,
    public val committer: GHCommit.GitUser,
    public val message: String,
    public val tree: GHTree
)
