package objects

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GHRelease(
    val url: String,
    val htmlUrl: String,
    val assetsUrl: String,
    val uploadUrl: String,
    val tarballUrl: String?,
    val zipballUrl: String?,
    val id: Int,
    val nodeId: String,
    val tagName: String,
    val targetCommitish: String,
    val name: String?,
    val body: String?,
    @SerialName("draft") val isDraft: Boolean,
    @SerialName("prerelease") val isPrerelease: Boolean,
    val createdAt: Instant,
    val publishedAt: Instant?,
    val author: GHUser,
    val assets: List<GHAsset>,
    val bodyHtml: String,
    val bodyText: String,
    val mentionsCount: Int,
    val discussionUrl: String,
    val reactions: Reactions
) {
    @Serializable
    class Reactions(
        val url: String,
        val totalCount: Int,
        @SerialName("+1") val upvotes: Int,
        @SerialName("-1") val downvotes: Int,
        val laugh: Int,
        val confused: Int,
        val heart: Int,
        val hooray: Int,
        val eyes: Int,
        val rocket: Int
    )
}
