package objects

import GHResult
import kotlinx.serialization.Serializable

@Serializable
public class GHOrganization internal constructor(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHOrganization

        if (login != other.login) return false
        if (url != other.url) return false
        if (reposUrl != other.reposUrl) return false
        if (eventsUrl != other.eventsUrl) return false
        if (hooksUrl != other.hooksUrl) return false
        if (issuesUrl != other.issuesUrl) return false
        if (membersUrl != other.membersUrl) return false
        if (publicMembersUrl != other.publicMembersUrl) return false
        if (avatarUrl != other.avatarUrl) return false
        return description == other.description
    }

    override fun hashCode(): Int {
        var result = login.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + reposUrl.hashCode()
        result = 31 * result + eventsUrl.hashCode()
        result = 31 * result + hooksUrl.hashCode()
        result = 31 * result + issuesUrl.hashCode()
        result = 31 * result + membersUrl.hashCode()
        result = 31 * result + publicMembersUrl.hashCode()
        result = 31 * result + avatarUrl.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHOrganization(login='$login', url='$url', reposUrl='$reposUrl', eventsUrl='$eventsUrl', hooksUrl='$hooksUrl', issuesUrl='$issuesUrl', membersUrl='$membersUrl', publicMembersUrl='$publicMembersUrl', avatarUrl='$avatarUrl', description=$description)"
    }
}
