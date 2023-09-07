package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.ApiException
import com.russellbanks.ktgithub.GHResult
import com.russellbanks.ktgithub.objects.sorts.SinceSort
import dev.drewhamilton.poko.Poko
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHUser(
    public val name: String? = null,
    public val email: String? = null,
    public val login: String,
    public val id: Int,
    public val htmlUrl: String,
    public val avatarUrl: String,
    public val followersUrl: String,
    public val followingUrl: String,
    public val gistsUrl: String,
    private val starredUrl: String,
    public val subscriptionsUrl: String,
    private val organizationsUrl: String,
    private val reposUrl: String,
    public val eventsUrl: String,
    private val receivedEventsUrl: String,
    public val type: String,
    public val sideAdmin: Boolean? = null,
    public val url: String,
    public val company: String? = null,
    @SerialName("blog") public val blogUrl: String? = null,
    public val location: String? = null,
    @SerialName("hireable") public val isHireable: Boolean? = null,
    public val bio: String? = null,
    public val twitterUsername: String? = null,
    @SerialName("public_repos") public val publicRepositoriesCount: Int? = null,
    @SerialName("public_gists") public val publicGistsCount: Int? = null,
    @SerialName("followers") public val followersCount: Int? = null,
    @SerialName("following") public val followingCount: Int? = null,
    public val createdAt: Instant? = null,
    public val updatedAt: Instant? = null,
    @SerialName("private_gists") public val privateGistsCount: Int? = null
) : GitHubObject() {
    public suspend fun fetchStarred(
        sort: SinceSort = SinceSort.CREATED,
        direction: Direction = Direction.DESCENDING,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHRepository>> = getWithConfig(starredUrl) {
        url.parameters.apply {
            append("sort", sort.toString())
            append("direction", direction.toString())
            append("per_page", perPage.toString())
            append("page", page.toString())
        }
    }

    /**
     * Fetches a GitHub repository using the specified owner and repository name.
     *
     * See [Repositories#get-a-repository](https://docs.github.com/rest/repos/repos#get-a-repository)
     *
     * @return A [GHResult] which can be one of the following:
     * - [GHResult.MovedPermanently]
     * - [GHResult.Forbidden]
     * - [GHResult.NotFound] if the repository does not exist
     */
    public suspend fun fetchRepositories(): GHResult<List<GHRepository>> = getWithConfig(reposUrl)

    /**
     * Returns a list of GitHub organizations the user is a member of
     *
     * @return A [Result] wrapping a [List] of [GHOrganization] instances on successful fetch,
     * or an [ApiException] containing the status code in case of a failure.
     */
    public suspend fun fetchOrganizations(): GHResult<List<GHOrganization>> = getWithConfig(organizationsUrl)
}
