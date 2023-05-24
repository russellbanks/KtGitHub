package com.russellbanks.ktgithub.objects

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GHRelease(
    public val url: String,
    public val htmlUrl: String,
    public val assetsUrl: String,
    public val uploadUrl: String,
    public val tarballUrl: String?,
    public val zipballUrl: String?,
    public val id: Int,
    public val nodeId: String,
    public val tagName: String,
    public val targetCommitish: String,
    public val name: String?,
    public val body: String?,
    @SerialName("draft") public val isDraft: Boolean,
    @SerialName("prerelease") public val isPrerelease: Boolean,
    public val createdAt: Instant,
    public val publishedAt: Instant?,
    public val author: GHUser,
    public val assets: List<GHAsset>,
    public val bodyHtml: String? = null,
    public val bodyText: String? = null,
    public val mentionsCount: Int? = null,
    public val discussionUrl: String? = null,
    public val reactions: Reactions? = null
) {
    @Serializable
    public class Reactions(
        public val url: String,
        public val totalCount: Int,
        @SerialName("+1") public val upvotes: Int,
        @SerialName("-1") public val downvotes: Int,
        public val laugh: Int,
        public val confused: Int,
        public val heart: Int,
        public val hooray: Int,
        public val eyes: Int,
        public val rocket: Int
    )
}
