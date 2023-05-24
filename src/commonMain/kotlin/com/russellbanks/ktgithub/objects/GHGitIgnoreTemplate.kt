package com.russellbanks.ktgithub.objects

import kotlinx.serialization.Serializable

@Serializable
public class GHGitIgnoreTemplate internal constructor(
    public val name: String,
    public val source: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHGitIgnoreTemplate

        if (name != other.name) return false
        return source == other.source
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + source.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHGitIgnoreTemplate(name='$name', source='$source')"
    }
}
