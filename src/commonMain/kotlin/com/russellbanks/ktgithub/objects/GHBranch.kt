package com.russellbanks.ktgithub.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GHBranch internal constructor(
    public val name: String,
    public val commit: GHCommit,
    @SerialName("_links") public val links: Links,
    @SerialName("protected") public val isProtected: Boolean,
    // val protection: Protection
) {
    @Serializable
    public class Links(
        public val html: String,
        public val self: String
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

    public val sha1: String get() = commit.sha
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
