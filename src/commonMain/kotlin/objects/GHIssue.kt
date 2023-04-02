package objects

import GHResult
import kotlinx.serialization.Serializable

@Serializable
public class GHIssue internal constructor(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHIssue

        if (url != other.url) return false
        if (repositoryUrl != other.repositoryUrl) return false
        if (labelsUrl != other.labelsUrl) return false
        if (commentsUrl != other.commentsUrl) return false
        if (eventsUrl != other.eventsUrl) return false
        if (htmlUrl != other.htmlUrl) return false
        if (number != other.number) return false
        if (state != other.state) return false
        if (stateReason != other.stateReason) return false
        if (title != other.title) return false
        if (body != other.body) return false
        return user == other.user
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + repositoryUrl.hashCode()
        result = 31 * result + labelsUrl.hashCode()
        result = 31 * result + commentsUrl.hashCode()
        result = 31 * result + eventsUrl.hashCode()
        result = 31 * result + htmlUrl.hashCode()
        result = 31 * result + number
        result = 31 * result + state.hashCode()
        result = 31 * result + (stateReason?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (user?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHIssue(url='$url', repositoryUrl='$repositoryUrl', labelsUrl='$labelsUrl', commentsUrl='$commentsUrl', eventsUrl='$eventsUrl', htmlUrl='$htmlUrl', number=$number, state=$state, stateReason=$stateReason, title='$title', body=$body, user=$user)"
    }
}
