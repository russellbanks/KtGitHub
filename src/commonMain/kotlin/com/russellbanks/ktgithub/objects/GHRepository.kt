package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.ApiException
import com.russellbanks.ktgithub.GHResult
import com.russellbanks.ktgithub.objects.sorts.ParameterConstants
import com.russellbanks.ktgithub.objects.sorts.RepositorySort
import com.russellbanks.ktgithub.objects.sorts.SinceSort
import dev.drewhamilton.poko.Poko
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHRepository internal constructor(
    /**
     * The name of the repository.
     */
    public val name: String,
    public val fullName: String,
    public val owner: GHUser,
    /**
     * Whether the repository is private or public.
     */
    public val private: Boolean,
    public val htmlUrl: String,
    public val description: String?,
    @SerialName("fork") public val isFork: Boolean,
    private val url: String,
    public val archiveUrl: String,
    public val assigneesUrl: String,
    public val blobsUrl: String,
    public val branchesUrl: String,
    private val collaboratorsUrl: String,
    private val commentsUrl: String,
    private val commitsUrl: String,
    public val compareUrl: String,
    public val contentsUrl: String,
    public val contributorsUrl: String,
    public val deploymentsUrl: String,
    public val downloadsUrl: String,
    public val eventsUrl: String,
    private val forksUrl: String,
    private val gitCommitsUrl: String,
    private val gitRefsUrl: String,
    private val gitTagsUrl: String,
    public val gitUrl: String,
    private val issueCommentUrl: String,
    private val issueEventsUrl: String,
    private val issuesUrl: String,
    private val keysUrl: String,
    private val labelsUrl: String,
    private val languagesUrl: String,
    private val mergedUrl: String? = null,
    private val milestonesUrl: String,
    private val notificationsUrl: String,
    private val pullsUrl: String,
    private val releasesUrl: String,
    private val sshUrl: String,
    private val stargazersUrl: String,
    private val statusesUrl: String,
    private val subscribersUrl: String,
    private val subscriptionUrl: String,
    private val tagsUrl: String,
    private val teamsUrl: String,
    private val treesUrl: String,
    private val cloneUrl: String,
    private val mirrorUrl: String?,
    private val hooksUrl: String,
    private val svnUrl: String,
    private val homePage: String? = null,
    private val language: String?,
    private val forksCount: Int,
    private val stargazersCount: Int,
    private val watchersCount: Int,
    /**
     * The size of the repository. Size is calculated hourly. When a repository is initially created, the size is 0.
     */
    private val size: Int,
    public val defaultBranch: String,
    private val openIssuesCount: Int,
    private val isTemplate: Boolean,
    private val topics: List<String>,
    private val hasIssues: Boolean,
    private val hasProjects: Boolean,
    private val hasWiki: Boolean,
    private val hasPages: Boolean,
    private val hasDownloads: Boolean,
    private val hasDiscussions: Boolean,
    public val archived: Boolean,
    /**
     * Returns whether this repository is disabled.
     */
    @SerialName("disabled") public val isDisabled: Boolean,
    public val visibility: String,
    public val pushedAt: Instant,
    public val createdAt: Instant,
    public val updatedAt: Instant,
    // val permissions: List<Permission>
    @SerialName("allow_rebase_merge") public val rebaseMergeAllowed: Boolean
) : GitHubObject() {
    public suspend fun fetchCommits(): GHResult<List<GHCommit>> = getWithConfig(commitsUrl.removeCurlyBraces())

    public suspend fun fetchIssues(): GHResult<List<GHIssue>> = getWithConfig(issuesUrl.removeCurlyBraces())

    public suspend fun fetchCollaborators(
        affiliation: Affiliation = Affiliation.ALL,
        permission: Permission? = null,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHUser>> = fetchUsersFromUrl(collaboratorsUrl, affiliation, permission, perPage, page)

    public suspend fun fetchContributors(
        affiliation: Affiliation = Affiliation.ALL,
        permission: Permission? = null,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHUser>> = fetchUsersFromUrl(contributorsUrl, affiliation, permission, perPage, page)

    private suspend fun fetchUsersFromUrl(
        urlString: String,
        affiliation: Affiliation = Affiliation.ALL,
        permission: Permission? = null,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHUser>> = getWithConfig(urlString.removeCurlyBraces()) {
        url.parameters.apply {
            append(ParameterConstants.affiliation, affiliation.toString())
            permission?.let { append(ParameterConstants.permission, it.toString()) }
            append(ParameterConstants.perPage, perPage.toString())
            append(ParameterConstants.page, page.toString())
        }
    }

    public suspend fun fetchComments(
        sort: SinceSort = SinceSort.CREATED,
        direction: Direction? = null,
        since: Instant? = null,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHComment>> {
        return getWithConfig(commentsUrl) {
            url.parameters.apply {
                append(ParameterConstants.sort, sort.toString())
                direction?.let { append(Direction.name, it.toString()) }
                since?.let { append(ParameterConstants.since, it.toString()) }
                append(ParameterConstants.perPage, perPage.toString())
                append(ParameterConstants.page, page.toString())
            }
        }
    }

    public suspend fun fetchForks(
        sort: RepositorySort = RepositorySort.NEWEST,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHRepository>> = getWithConfig(forksUrl) {
        url.parameters.apply {
            append(ParameterConstants.sort, sort.toString())
            append(ParameterConstants.perPage, perPage.toString())
            append(ParameterConstants.page, page.toString())
        }
    }

    public suspend fun fork(
        organization: GHOrganization? = null,
        newName: String? = null,
        defaultBranchOnly: Boolean = false
    ): GHResult<GHRepository> = postWithConfig(forksUrl) {
        @Serializable
        class ForkParameters(
            val organization: String?,
            val name: String?,
            val defaultBranchOnly: Boolean
        )
        setBody(ForkParameters(organization?.login, newName, defaultBranchOnly))
    }

    /**
     * Deletes this repository on GitHub.
     *
     * See [Repositories#delete-a-repository](https://docs.github.com/rest/reference/repos#delete-a-repository)
     *
     * Deleting a repository requires admin access. If OAuth is used, the delete_repo scope is required.
     * If an organization owner has configured the organization to prevent members from deleting
     * organization-owned repositories, a member will get a 403 Forbidden response.
     *
     * @return A [GHResult] indicating the success or failure of the repository deletion.
     *         Possible [ApiException] statuses include:
     * - [HttpStatusCode.TemporaryRedirect]: The requested resource has moved temporarily.
     * - [HttpStatusCode.Forbidden]: The request is forbidden due to insufficient permissions or organization-level
     * restrictions on deleting repositories.
     * - [HttpStatusCode.NotFound]: The repository does not exist.
     */
    public suspend fun delete(): GHResult<Unit> = root.deleteRepository(owner = owner.login, repository = name)

    /**
     * Updates this repository with the provided parameters.
     *
     * See [Repositories#update-a-repository](https://docs.github.com/rest/repos/repos#update-a-repository)
     *
     * @param name The name of the repository.
     * @param description A short description of the repository.
     * @param homepage A URL with more information about the repository.
     * @param private Either true to make the repository private or false to make it public.
     * @param hasIssues Either true to enable issues for this repository or false to disable them.
     * @param hasProjects Either true to enable projects for this repository or false to disable them.
     * @param hasWiki Either true to enable the wiki for this repository or false to disable it.
     * @param isTemplate Either true to make this repo available as a template repository or false to prevent it.
     * @param defaultBranch Updates the default branch for this repository.
     * @param allowSquashMerge Either true to allow squash-merging pull requests, or false to prevent squash-merging.
     * @param allowMergeCommit Either true to allow merging pull requests with a merge commit, or false to prevent
     * merging pull requests with merge commits.
     * @param allowRebaseMerge Either true to allow rebase-merging pull requests, or false to prevent rebase-merging.
     * @param allowAutoMerge Either true to allow auto-merge on pull requests, or false to disallow auto-merge.
     * @param deleteBranchOnMerge Either true to allow automatically deleting head branches when pull requests are
     * merged, or false to prevent automatic deletion.
     * @param allowUpdateBranch Either true to always allow a pull request head branch that is behind its base branch to
     * be updated even if it is not required to be up-to-date before merging, or false otherwise.
     * @param useSquashPrTitleAsDefault Either true to allow squash-merge commits to use pull request title, or false to
     * use commit message. **This property has been deprecated. Please use squash_merge_commit_title instead.
     * @param archived Whether to archive this repository. false will unarchive a previously archived repository.
     * @param allowForking Either true to allow private forks, or false to prevent private forks.
     * @param webCommitSignoffRequired Either true to require contributors to sign off on web-based commits, or false to
     * not require contributors to sign off on web-based commits.
     * @return A [GHResult] containing a [GHRepository] object representing the updated repository,
     * if the request is successful, or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.TemporaryRedirect]: The repository was moved to a new location.
     * - [HttpStatusCode.Forbidden]: The request is not allowed (e.g., insufficient permissions).
     * - [HttpStatusCode.NotFound]: The requested repository was not found.
     * - [HttpStatusCode.UnprocessableEntity]: Validation failed, or the endpoint has been spammed.
     */
    public suspend fun update(
        name: String? = null,
        description: String? = null,
        homepage: String? = null,
        private: Boolean? = null,
        // visibility: Visi
        // securityAndAnalysis
        hasIssues: Boolean? = null,
        hasProjects: Boolean? = null,
        hasWiki: Boolean? = null,
        isTemplate: Boolean? = null,
        defaultBranch: String? = null,
        allowSquashMerge: Boolean? = null,
        allowMergeCommit: Boolean? = null,
        allowRebaseMerge: Boolean? = null,
        allowAutoMerge: Boolean? = null,
        deleteBranchOnMerge: Boolean? = null,
        allowUpdateBranch: Boolean? = null,
        useSquashPrTitleAsDefault: Boolean? = null,
        // squashMergeCommitTitle:
        // mergeCommitTitle:
        // mergeCommitMessage:
        archived: Boolean? = null,
        allowForking: Boolean? = null,
        webCommitSignoffRequired: Boolean? = null
    ): GHResult<GHRepository> = root.updateRepository(
        owner.login, this.name, name, description, homepage, private, hasIssues, hasProjects, hasWiki, isTemplate,
        defaultBranch, allowSquashMerge, allowMergeCommit, allowRebaseMerge, allowAutoMerge, deleteBranchOnMerge,
        allowUpdateBranch, useSquashPrTitleAsDefault, archived, allowForking, webCommitSignoffRequired
    )

    /**
     * Creates a tree in this repository.
     *
     * See [Trees#Create-a-tree](https://docs.github.com/rest/git/trees#create-a-tree)
     *
     * @param baseTreeSha TOD
     * @param treeItems A list of [GHTree.Item] objects specifying the tree structure.
     * @return A [GHResult] containing the created [GHTree] or an [ApiException] with the following possible statuses:
     * - [HttpStatusCode.Forbidden]: The operation is forbidden.
     * - [HttpStatusCode.NotFound]: The resource is not found.
     * - [HttpStatusCode.UnprocessableEntity]: Validation failed or the endpoint has been spammed.
     */
    public suspend fun createTree(baseTreeSha: String, treeItems: List<GHTree.Item>): GHResult<GHTree> {
        return root.createTree(owner.login, name, baseTreeSha, treeItems)
    }

    public suspend fun createTree(baseTree: GHBranch, treeItems: List<GHTree.Item>): GHResult<GHTree> {
        return createTree(baseTree.sha1, treeItems)
    }

    public suspend fun fetchBranch(branchName: String): GHResult<GHBranch> = root.fetchBranch(owner.login, name, branchName)

    public suspend fun createCommit(commitMessage: String, tree: GHTree): GHResult<GHCommitResponse> {
        return root.createCommit(owner.login, name, commitMessage, tree)
    }

    public suspend fun fetchReference(reference: String): GHResult<GHRef> = root.fetchReference(owner.login, name, reference)

    public suspend fun deleteReference(reference: String): GHResult<Unit> = root.deleteReference(owner.login, name, reference)

    public enum class Affiliation {
        OUTSIDE,
        DIRECT,
        ALL;

        override fun toString(): String = name.lowercase()
    }

    public enum class Permission {
        PULL,
        TRIAGE,
        PUSH,
        MAINTAIN,
        ADMIN;

        override fun toString(): String = name.lowercase()
    }

    private fun String.removeCurlyBraces(): String = replace("\\{.*?}".toRegex(), "")
}
