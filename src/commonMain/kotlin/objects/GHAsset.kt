package objects

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GHAsset(
    val url: String,
    val browserDownloadUrl: String,
    val id: Int,
    val nodeId: String,
    val name: String,
    val label: String?,
    val state: State,
    val contentType: String,
    val size: Int,
    val downloadCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val uploader: GHUser?
) {
    @Serializable
    enum class State {
        @SerialName("uploaded") UPLOADED,
        @SerialName("open") OPEN
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHAsset

        if (url != other.url) return false
        if (browserDownloadUrl != other.browserDownloadUrl) return false
        if (id != other.id) return false
        if (nodeId != other.nodeId) return false
        if (name != other.name) return false
        if (label != other.label) return false
        if (state != other.state) return false
        if (contentType != other.contentType) return false
        if (size != other.size) return false
        if (downloadCount != other.downloadCount) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        return uploader == other.uploader
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + browserDownloadUrl.hashCode()
        result = 31 * result + id
        result = 31 * result + nodeId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (label?.hashCode() ?: 0)
        result = 31 * result + state.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + size
        result = 31 * result + downloadCount
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + (uploader?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHAsset(url='$url', browserDownloadUrl='$browserDownloadUrl', id=$id, nodeId='$nodeId', name='$name', label=$label, state=$state, contentType='$contentType', size=$size, downloadCount=$downloadCount, createdAt=$createdAt, updatedAt=$updatedAt, uploader=$uploader)"
    }
}
