package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.GHResult
import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHRef internal constructor(
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
}
