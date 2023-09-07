package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHCommit internal constructor(
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
    @Poko public class Details private constructor(
        public val author: GitUser?,
        public val committer: GitUser?,
        public val message: String,
        public val commentCount: Int,
        public val tree: GHTree
    )

    @Serializable
    @Poko public class GitUser private constructor(
        public val name: String,
        public val email: String,
        public val date: Instant
    )

    @Serializable
    @Poko public class Stats(
        public val additions: Int,
        public val deletions: Int,
        public val total: Int
    )

    @Serializable
    @Poko public class DiffEntry(
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
    }
}
