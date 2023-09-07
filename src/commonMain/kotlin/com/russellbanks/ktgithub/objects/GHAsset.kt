package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHAsset(
    public val url: String,
    public val browserDownloadUrl: String,
    public val id: Int,
    public val nodeId: String,
    public val name: String,
    public val label: String?,
    public val state: State,
    public val contentType: String,
    public val size: Int,
    public val downloadCount: Int,
    public val createdAt: Instant,
    public val updatedAt: Instant,
    public val uploader: GHUser?
) {
    @Serializable
    public enum class State {
        @SerialName("uploaded") UPLOADED,
        @SerialName("open") OPEN
    }
}
