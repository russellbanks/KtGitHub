package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.GHResult
import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHIssue internal constructor(
    public val url: String,
    private val repositoryUrl: String,
    public val labelsUrl: String,
    private val commentsUrl: String,
    public val eventsUrl: String,
    public val htmlUrl: String,
    public val number: Int,
    public val state: State,
    public val stateReason: String?,
    public val title: String,
    public val body: String?,
    public val user: GHUser?
) : GitHubObject() {
    @Serializable
    public enum class State {
        OPEN,
        CLOSED,
        ALL;
    }

    /**
     * Fetches the GitHub repository of the given issue.
     *
     * See [Repositories#get-a-repository](https://docs.github.com/rest/repos/repos#get-a-repository)
     *
     * @return A [GHResult] which can be one of the following:
     * - [GHResult.Success] containing the fetched [GHRepository]
     * - [GHResult.MovedPermanently]
     * - [GHResult.Forbidden]
     * - [GHResult.NotFound] if the repository does not exist
     */
    public suspend fun fetchRepository(): GHResult<GHRepository> = getWithConfig(repositoryUrl)

    public suspend fun fetchComments(): GHResult<List<GHComment>> = getWithConfig(commentsUrl)
}
