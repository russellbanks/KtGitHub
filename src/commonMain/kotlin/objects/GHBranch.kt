package objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GHBranch internal constructor(
    val name: String,
    val commit: GHCommit,
    @SerialName("_links") val links: Links,
    @SerialName("protected") val isProtected: Boolean,
    // val protection: Protection
) {
    @Serializable
    class Links(
        val html: String,
        val self: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Links

            if (html != other.html) return false
            return self == other.self
        }

        override fun hashCode(): Int {
            var result = html.hashCode()
            result = 31 * result + self.hashCode()
            return result
        }

        override fun toString(): String {
            return "Links(html='$html', self='$self')"
        }
    }

    val sha1 get() = commit.sha
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHBranch

        if (name != other.name) return false
        if (commit != other.commit) return false
        if (links != other.links) return false
        return isProtected == other.isProtected
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + commit.hashCode()
        result = 31 * result + links.hashCode()
        result = 31 * result + isProtected.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHBranch(name='$name', commit=$commit, links=$links, isProtected=$isProtected)"
    }
}
