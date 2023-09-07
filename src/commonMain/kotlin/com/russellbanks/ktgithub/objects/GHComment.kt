package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
@Serializable
@Poko public class GHComment internal constructor(
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
}