package objects

import GHResult
import kotlinx.serialization.Serializable

@Serializable
class GHRef internal constructor(
    val ref: String,
    val nodeId: String,
    val url: String,
    val `object`: Object,
) : GitHubObject() {
    private val ownerRepository = url.substringAfter("repos/").substringBefore("/git").split('/')

    val owner = ownerRepository.first()

    val repository = ownerRepository[1]

    @Serializable
    class Object(
        val type: String,
        val sha: String,
        val url: String
    )

    suspend fun updateTo(sha: String, force: Boolean = false): GHResult<GHRef> {
        return root.updateReference(owner, repository, ref, sha, force)
    }

    suspend fun delete(): GHResult<Unit> = deleteWithConfig(url)

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
