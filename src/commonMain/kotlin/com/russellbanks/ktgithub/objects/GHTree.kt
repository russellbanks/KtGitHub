package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHTree private constructor(
    public val sha: String,
    public val url: String,
    @SerialName("truncated") public val isTruncated: Boolean = false,
    @SerialName("tree") public val items: List<Item>? = null
) {
    @Serializable
    @Poko public class Item internal constructor(
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
    }
}
