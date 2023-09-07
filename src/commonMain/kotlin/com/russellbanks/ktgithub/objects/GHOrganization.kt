package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.GHResult
import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHOrganization internal constructor(
    public val login: String,
    public val url: String,
    private val reposUrl: String,
    private val eventsUrl: String,
    private val hooksUrl: String,
    public val issuesUrl: String,
    private val membersUrl: String,
    private val publicMembersUrl: String,
    private val avatarUrl: String,
    private val description: String?
) : GitHubObject() {
    public suspend fun fetchRepositories(): GHResult<List<GHRepository>> = getWithConfig(reposUrl)
}
