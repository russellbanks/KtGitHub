package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.ApiException
import com.russellbanks.ktgithub.GHResult
import com.russellbanks.ktgithub.objects.sorts.SinceSort
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GHUser(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHUser

        if (name != other.name) return false
        if (email != other.email) return false
        if (login != other.login) return false
        if (id != other.id) return false
        if (htmlUrl != other.htmlUrl) return false
        if (avatarUrl != other.avatarUrl) return false
        if (followersUrl != other.followersUrl) return false
        if (followingUrl != other.followingUrl) return false
        if (gistsUrl != other.gistsUrl) return false
        if (starredUrl != other.starredUrl) return false
        if (subscriptionsUrl != other.subscriptionsUrl) return false
        if (organizationsUrl != other.organizationsUrl) return false
        if (reposUrl != other.reposUrl) return false
        if (eventsUrl != other.eventsUrl) return false
        if (receivedEventsUrl != other.receivedEventsUrl) return false
        if (type != other.type) return false
        if (sideAdmin != other.sideAdmin) return false
        if (url != other.url) return false
        if (company != other.company) return false
        if (blogUrl != other.blogUrl) return false
        if (location != other.location) return false
        if (isHireable != other.isHireable) return false
        if (bio != other.bio) return false
        if (twitterUsername != other.twitterUsername) return false
        if (publicRepositoriesCount != other.publicRepositoriesCount) return false
        if (publicGistsCount != other.publicGistsCount) return false
        if (followersCount != other.followersCount) return false
        if (followingCount != other.followingCount) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        return privateGistsCount == other.privateGistsCount
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + login.hashCode()
        result = 31 * result + id
        result = 31 * result + htmlUrl.hashCode()
        result = 31 * result + avatarUrl.hashCode()
        result = 31 * result + followersUrl.hashCode()
        result = 31 * result + followingUrl.hashCode()
        result = 31 * result + gistsUrl.hashCode()
        result = 31 * result + starredUrl.hashCode()
        result = 31 * result + subscriptionsUrl.hashCode()
        result = 31 * result + organizationsUrl.hashCode()
        result = 31 * result + reposUrl.hashCode()
        result = 31 * result + eventsUrl.hashCode()
        result = 31 * result + receivedEventsUrl.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (sideAdmin?.hashCode() ?: 0)
        result = 31 * result + url.hashCode()
        result = 31 * result + (company?.hashCode() ?: 0)
        result = 31 * result + (blogUrl?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (isHireable?.hashCode() ?: 0)
        result = 31 * result + (bio?.hashCode() ?: 0)
        result = 31 * result + (twitterUsername?.hashCode() ?: 0)
        result = 31 * result + (publicRepositoriesCount ?: 0)
        result = 31 * result + (publicGistsCount ?: 0)
        result = 31 * result + (followersCount ?: 0)
        result = 31 * result + (followingCount ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (privateGistsCount ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHUser(name=$name, email=$email, login='$login', id=$id, htmlUrl='$htmlUrl', avatarUrl='$avatarUrl', followersUrl='$followersUrl', followingUrl='$followingUrl', gistsUrl='$gistsUrl', starredUrl='$starredUrl', subscriptionsUrl='$subscriptionsUrl', organizationsUrl='$organizationsUrl', reposUrl='$reposUrl', eventsUrl='$eventsUrl', receivedEventsUrl='$receivedEventsUrl', type='$type', sideAdmin=$sideAdmin, url='$url', company=$company, blogUrl=$blogUrl, location=$location, isHireable=$isHireable, bio=$bio, twitterUsername=$twitterUsername, publicRepositoriesCount=$publicRepositoriesCount, publicGistsCount=$publicGistsCount, followersCount=$followersCount, followingCount=$followingCount, createdAt=$createdAt, updatedAt=$updatedAt, privateGistsCount=$privateGistsCount)"
    }
}
