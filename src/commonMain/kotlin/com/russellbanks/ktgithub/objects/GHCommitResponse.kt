package com.russellbanks.ktgithub.objects

import kotlinx.serialization.Serializable

@Serializable
public class GHCommitResponse(
    public val sha: String,
    public val nodeId: String,
    public val url: String,
    public val author: GHCommit.GitUser,
    public val committer: GHCommit.GitUser,
    public val message: String,
    public val tree: GHTree
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHCommitResponse

        if (sha != other.sha) return false
        if (nodeId != other.nodeId) return false
        if (url != other.url) return false
        if (author != other.author) return false
        if (committer != other.committer) return false
        if (message != other.message) return false
        return tree == other.tree
    }

    override fun hashCode(): Int {
        var result = sha.hashCode()
        result = 31 * result + nodeId.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + committer.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + tree.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHCommitResponse(sha='$sha', nodeId='$nodeId', url='$url', author=$author, committer=$committer, message='$message', tree=$tree)"
    }
}
