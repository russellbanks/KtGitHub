package objects

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
@Serializable
public class GHComment internal constructor(
    public val body: String,
    public val user: GHUser,
    public val createdAt: Instant,
    public val updatedAt: Instant,
    public val issueUrl: String,
    public val authorAssociation: AuthorAssociation
) {
    public enum class AuthorAssociation {
        COLLABORATOR,
        CONTRIBUTOR,
        FIRST_TIMER,
        FIRST_TIME_CONTRIBUTOR,
        MANNEQUIN,
        MEMBER,
        NONE,
        OWNER;

        override fun toString(): String = name.replace('_', ' ').lowercase().replaceFirstChar(Char::titlecase)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHComment

        if (body != other.body) return false
        if (user != other.user) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (issueUrl != other.issueUrl) return false
        return authorAssociation == other.authorAssociation
    }

    override fun hashCode(): Int {
        var result = body.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + issueUrl.hashCode()
        result = 31 * result + authorAssociation.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHComment(body='$body', user=$user, createdAt=$createdAt, updatedAt=$updatedAt, issueUrl='$issueUrl', authorAssociation=$authorAssociation)"
    }
}