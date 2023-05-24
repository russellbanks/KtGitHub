package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.GHResult
import kotlinx.serialization.Serializable

@Serializable
public class GHRef internal constructor(
    public val ref: String,
    public val nodeId: String,
    public val url: String,
    public val `object`: Object,
) : GitHubObject() {
    private val ownerRepository = url.substringAfter("repos/").substringBefore("/git").split('/')

    public val owner: String = ownerRepository.first()

    public val repository: String = ownerRepository[1]

    @Serializable
    public class Object(
        public val type: String,
        public val sha: String,
        public val url: String
    )

    public suspend fun updateTo(sha: String, force: Boolean = false): GHResult<GHRef> {
        return root.updateReference(owner, repository, ref, sha, force)
    }

    public suspend fun delete(): GHResult<Unit> = deleteWithConfig(url)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHRef

        if (ref != other.ref) return false
        if (nodeId != other.nodeId) return false
        if (url != other.url) return false
        return `object` == other.`object`
    }

    override fun hashCode(): Int {
        var result = ref.hashCode()
        result = 31 * result + nodeId.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + `object`.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHRef(ref='$ref', nodeId='$nodeId', url='$url', `object`=$`object`)"
    }
}
