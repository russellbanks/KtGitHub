package com.russellbanks.ktgithub.objects

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
public class GHCommit internal constructor(
    public val url: String,
    public val sha: String,
    public val nodeId: String,
    public val htmlUrl: String,
    public val commentsUrl: String,
    public val commit: Details,
    public val author: GHUser,
    public val committer: GHUser,
    public val stats: Stats? = null,
    public val files: List<DiffEntry>? = null
) {
    @Serializable
    public class Details private constructor(
        public val author: GitUser?,
        public val committer: GitUser?,
        public val message: String,
        public val commentCount: Int,
        public val tree: GHTree
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Details

            if (author != other.author) return false
            if (committer != other.committer) return false
            if (message != other.message) return false
            if (commentCount != other.commentCount) return false
            return tree == other.tree
        }

        override fun hashCode(): Int {
            var result = author?.hashCode() ?: 0
            result = 31 * result + (committer?.hashCode() ?: 0)
            result = 31 * result + message.hashCode()
            result = 31 * result + commentCount
            result = 31 * result + tree.hashCode()
            return result
        }

        override fun toString(): String {
            return "Details(author=$author, committer=$committer, message='$message', commentCount=$commentCount, tree=$tree)"
        }
    }

    @Serializable
    public class GitUser private constructor(
        public val name: String,
        public val email: String,
        public val date: Instant
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as GitUser

            if (name != other.name) return false
            if (email != other.email) return false
            return date == other.date
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + email.hashCode()
            result = 31 * result + date.hashCode()
            return result
        }

        override fun toString(): String {
            return "GitUser(name='$name', email='$email', date=$date)"
        }
    }

    @Serializable
    public class Stats(
        public val additions: Int,
        public val deletions: Int,
        public val total: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Stats

            if (additions != other.additions) return false
            if (deletions != other.deletions) return false
            return total == other.total
        }

        override fun hashCode(): Int {
            var result = additions
            result = 31 * result + deletions
            result = 31 * result + total
            return result
        }

        override fun toString(): String {
            return "Stats(additions=$additions, deletions=$deletions, total=$total)"
        }
    }

    @Serializable
    public class DiffEntry(
        public val sha: String,
        public val filename: String,
        public val status: Status,
        public val additions: Int,
        public val deletions: Int,
        public val changes: Int,
        public val blobUrl: String,
        public val rawUrl: String,
        public val contentsUrl: String,
        public val patch: String?,
        public val previousFilename: String?
    ) {
        public enum class Status {
            ADDED,
            REMOVED,
            MODIFIED,
            RENAMED,
            COPIED,
            CHANGED,
            UNCHANGED
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as DiffEntry

            if (sha != other.sha) return false
            if (filename != other.filename) return false
            if (status != other.status) return false
            if (additions != other.additions) return false
            if (deletions != other.deletions) return false
            if (changes != other.changes) return false
            if (blobUrl != other.blobUrl) return false
            if (rawUrl != other.rawUrl) return false
            if (contentsUrl != other.contentsUrl) return false
            if (patch != other.patch) return false
            return previousFilename == other.previousFilename
        }

        override fun hashCode(): Int {
            var result = sha.hashCode()
            result = 31 * result + filename.hashCode()
            result = 31 * result + status.hashCode()
            result = 31 * result + additions
            result = 31 * result + deletions
            result = 31 * result + changes
            result = 31 * result + blobUrl.hashCode()
            result = 31 * result + rawUrl.hashCode()
            result = 31 * result + contentsUrl.hashCode()
            result = 31 * result + (patch?.hashCode() ?: 0)
            result = 31 * result + (previousFilename?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "DiffEntry(sha='$sha', filename='$filename', status=$status, additions=$additions, deletions=$deletions, changes=$changes, blobUrl='$blobUrl', rawUrl='$rawUrl', contentsUrl='$contentsUrl', patch=$patch, previousFilename=$previousFilename)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHCommit

        if (url != other.url) return false
        if (sha != other.sha) return false
        if (nodeId != other.nodeId) return false
        if (htmlUrl != other.htmlUrl) return false
        if (commentsUrl != other.commentsUrl) return false
        if (commit != other.commit) return false
        if (author != other.author) return false
        if (committer != other.committer) return false
        if (stats != other.stats) return false
        return files == other.files
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + sha.hashCode()
        result = 31 * result + nodeId.hashCode()
        result = 31 * result + htmlUrl.hashCode()
        result = 31 * result + commentsUrl.hashCode()
        result = 31 * result + commit.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + committer.hashCode()
        result = 31 * result + (stats?.hashCode() ?: 0)
        result = 31 * result + (files?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHCommit(url='$url', sha='$sha', nodeId='$nodeId', htmlUrl='$htmlUrl', commentsUrl='$commentsUrl', commit=$commit, author=$author, committer=$committer, stats=$stats, files=$files)"
    }
}
