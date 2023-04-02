package objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GHTree private constructor(
    public val sha: String,
    public val url: String,
    @SerialName("truncated") public val isTruncated: Boolean = false,
    @SerialName("tree") public val items: List<Item>? = null
) {
    @Serializable
    public class Item internal constructor(
        public val path: String,
        public val mode: Mode,
        public val type: Type,
        public val sha: String,
        public val size: Int? = null,
        public val url: String,
    ) {
        internal var content: String? = null

        public constructor(
            path: String,
            content: String?,
            isExecutable: Boolean
        ) : this(path, if (isExecutable) Mode.EXECUTABLE else Mode.FILE, Type.BLOB, "", 0, "") {
            this.content = content
        }

        @Serializable
        public enum class Mode(public val number: String) {
            @SerialName("100644") FILE("100644"),
            @SerialName("100755") EXECUTABLE("100755"),
            @SerialName("040000") SUBDIRECTORY("040000"),
            @SerialName("160000") SUBMODULE("160000"),
            @SerialName("120000") SYMLINK("120000")
        }

        @Serializable
        public enum class Type {
            @SerialName("blob") BLOB,
            @SerialName("tree") TREE,
            @SerialName("commit") COMMIT;

            override fun toString(): String = name.lowercase()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Item

            if (path != other.path) return false
            if (mode != other.mode) return false
            if (type != other.type) return false
            if (sha != other.sha) return false
            if (size != other.size) return false
            if (url != other.url) return false
            return content == other.content
        }

        override fun hashCode(): Int {
            var result = path.hashCode()
            result = 31 * result + mode.hashCode()
            result = 31 * result + type.hashCode()
            result = 31 * result + sha.hashCode()
            result = 31 * result + (size ?: 0)
            result = 31 * result + url.hashCode()
            result = 31 * result + (content?.hashCode() ?: 0)
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHTree

        if (sha != other.sha) return false
        if (url != other.url) return false
        if (isTruncated != other.isTruncated) return false
        return items == other.items
    }

    override fun hashCode(): Int {
        var result = sha.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + isTruncated.hashCode()
        result = 31 * result + (items?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHTree(sha='$sha', url='$url', isTruncated=$isTruncated, items=$items)"
    }
}
