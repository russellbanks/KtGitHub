package com.russellbanks.ktgithub

import com.russellbanks.ktgithub.objects.GHBranch
import com.russellbanks.ktgithub.objects.GHCommitResponse
import com.russellbanks.ktgithub.objects.GHContent
import com.russellbanks.ktgithub.objects.GHGist
import com.russellbanks.ktgithub.objects.GHGitIgnoreTemplate
import com.russellbanks.ktgithub.objects.GHIssue
import com.russellbanks.ktgithub.objects.GHLicense
import com.russellbanks.ktgithub.objects.GHRateLimitOverview
import com.russellbanks.ktgithub.objects.GHRef
import com.russellbanks.ktgithub.objects.GHRelease
import com.russellbanks.ktgithub.objects.GHRepository
import com.russellbanks.ktgithub.objects.GHTree
import com.russellbanks.ktgithub.objects.GHUser
import com.russellbanks.ktgithub.objects.GitHubObject
import com.russellbanks.ktgithub.objects.sorts.ParameterConstants
import dev.drewhamilton.poko.Poko
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@OptIn(ExperimentalStdlibApi::class)
public class GitHub internal constructor(
    internal var token: String?,
    engine: HttpClientEngine,
    logger: (String) -> Unit = {},
    logLevel: LogLevel = LogLevel.INFO,
): AutoCloseable, GitHubObject() {
    init {
        root = this
    }

    @OptIn(ExperimentalSerializationApi::class)
    internal var client: HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                namingStrategy = JsonNamingStrategy.SnakeCase
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(Logging) {
            this.logger = object: Logger {
                override fun log(message: String) {
                    logger(message)
                }
            }
            level = logLevel
        }
    }

    /**
     * Fetches the repository data from GitHub for a given owner and repository name.
     *
     * See [Repositories#get-a-repository](https://docs.github.com/rest/repos/repos#get-a-repository)
     *
     * @param owner The owner of the repository, usually a username or an organization name.
     * @param repository The name of the repository.
     * @return A [GHResult] containing a [GHRepository] object if the request is successful,
     * or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.MovedPermanently]: The requested resource has moved permanently.
     * - [HttpStatusCode.Forbidden]: The request is forbidden due to rate limiting, missing authentication, or
     * insufficient permissions.
     * - [HttpStatusCode.NotFound]: The repository does not exist.
     */
    public suspend fun fetchRepository(owner: String, repository: String): GHResult<GHRepository> {
        return getWithConfig<GHRepository>("$API_URL/repos/$owner/$repository")
    }

    /**
     * Deletes a repository on GitHub for a given owner and repository name.
     *
     * See [Repositories#delete-a-repository](https://docs.github.com/rest/reference/repos#delete-a-repository)
     *
     * Deleting a repository requires admin access. If OAuth is used, the delete_repo scope is required.
     * If an organization owner has configured the organization to prevent members from deleting
     * organization-owned repositories, a member will get a 403 Forbidden response.
     *
     * @param owner The owner of the repository, usually a username or an organization name. The name is
     * case-insensitive.
     * @param repository The name of the repository. The name is case-insensitive.
     * @return A [GHResult] indicating the success or failure of the repository deletion.
     *         Possible [ApiException] statuses include:
     * - [HttpStatusCode.TemporaryRedirect]: The requested resource has moved temporarily.
     * - [HttpStatusCode.Forbidden]: The request is forbidden due to insufficient permissions or organization-level
     * restrictions on deleting repositories.
     * - [HttpStatusCode.NotFound]: The repository does not exist.
     */
    public suspend fun deleteRepository(owner: String, repository: String): GHResult<Unit> {
        return deleteWithConfig("$API_URL/repos/$owner/$repository")
    }

    /**
     * Updates a repository with the provided parameters.
     *
     * See [Repositories#update-a-repository](https://docs.github.com/rest/repos/repos#update-a-repository)
     *
     * @param owner The account owner of the repository. The name is case-insensitive.
     * @param repository The name of the repository. The name is case-insensitive.
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
     * be updated even if it is not required to be up to date before merging, or false otherwise.
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
    public suspend fun updateRepository(
        owner: String,
        repository: String,
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
    ): GHResult<GHRepository> {
        @Serializable
        class UpdateRepositoryParameters(
            val name: String? = null,
            val description: String? = null,
            val homepage: String? = null,
            val private: Boolean? = null,
            // val visibility: Visibility? = null
            // securityAndAnalysis
            val hasIssues: Boolean? = null,
            val hasProjects: Boolean? = null,
            val hasWiki: Boolean? = null,
            val isTemplate: Boolean? = null,
            val defaultBranch: String? = null,
            val allowSquashMerge: Boolean? = null,
            val allowMergeCommit: Boolean? = null,
            val allowRebaseMerge: Boolean? = null,
            val allowAutoMerge: Boolean? = null,
            val deleteBranchOnMerge: Boolean? = null,
            val allowUpdateBranch: Boolean? = null,
            val useSquashPrTitleAsDefault: Boolean? = null,
            // val squashMergeCommitTitle:
            // val mergeCommitTitle:
            // val mergeCommitMessage:
            val archived: Boolean? = null,
            val allowForking: Boolean? = null,
            val webCommitSignoffRequired: Boolean? = null
        )
        return patchWithConfig("$API_URL/repos/$owner/$repository") {
            setBody(
                UpdateRepositoryParameters(
                    name, description, homepage, private, hasIssues, hasProjects, hasWiki, isTemplate, defaultBranch,
                    allowSquashMerge, allowMergeCommit, allowRebaseMerge, allowAutoMerge, deleteBranchOnMerge,
                    allowUpdateBranch, useSquashPrTitleAsDefault, archived, allowForking, webCommitSignoffRequired
                )
            )
        }
    }

    public suspend fun fetchDirectoryContent(owner: String, repository: String, path: String): GHResult<List<GHContent>> {
        return getWithConfig("$API_URL/repos/$owner/$repository/contents/$path")
    }

    /**
     * Fetches publicly available information about a GitHub user with the specified username.
     *
     * See [Users#get-a-user](https://docs.github.com/rest/users/users#get-a-user)
     *
     * GitHub Apps with the Plan user permission can use this endpoint to retrieve information about
     * a user's GitHub plan. The GitHub App must be authenticated as a user.
     * See "Identifying and authorizing users for GitHub Apps" for details about authentication.
     *
     * The email key in the response is the publicly visible email address from the user's GitHub profile page.
     * If the user does not set a public email address, the value will be null.
     *
     * @param username The handle for the GitHub user account. The username is case-insensitive.
     * @return A [GHResult] containing a [GHUser] object if the request is successful,
     * or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.NotFound]: The user with the specified username does not exist or the resource is not found.
     */
    public suspend fun fetchUser(username: String): GHResult<GHUser> = getWithConfig("$API_URL/users/$username")

    /**
     * Fetches a list of all emojis available for use on GitHub.
     *
     * See [Emojis#get-emojis](https://docs.github.com/rest/emojis#get-emojis)
     *
     * @return A [GHResult] containing a [Map] where the key is the emoji's short code and the value is its URL,
     * if the request is successful, or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.NotModified]: The request was successful, but the list of emojis has not been modified
     * since the last request.
     */
    public suspend fun fetchAllEmojis(): GHResult<Map<String, String>> = getWithConfig("$API_URL/emojis")

    /**
     * Fetches the issue data for a specific issue in a given repository.
     *
     * See [Issues#get-an-issue](https://docs.github.com/rest/issues/issues#get-an-issue)
     *
     * Note: GitHub's REST API considers every pull request an issue, but not every issue is a pull request.
     * For this reason, "Issues" endpoints may return both issues and pull requests in the response.
     * You can identify pull requests by the pull_request key.
     *
     * @param owner The account owner of the repository. The name is not case sensitive.
     * @param repository The name of the repository. The name is not case sensitive.
     * @param issueNumber The number that identifies the issue.
     * @return A [GHResult] containing a [GHIssue] object if the request is successful,
     * or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.MovedPermanently]: The issue was transferred to another repository.
     * - [HttpStatusCode.NotFound]: The issue was transferred to or deleted from a repository where
     * the authenticated user lacks read access, or the resource is not found.
     * - [HttpStatusCode.Gone]: The issue was deleted from a repository where the authenticated user has read access.
     */
    public suspend fun fetchIssue(owner: String, repository: String, issueNumber: Int): GHResult<GHIssue> {
        return getWithConfig("$API_URL/repos/$owner/$repository/issues/$issueNumber")
    }

    /**
     * Fetches the available assignees for issues in a given repository.
     *
     * See [Assignees#list-assignees](https://docs.github.com/rest/issues/assignees#list-assignees)
     *
     * @param owner The account owner of the repository. The name is case-insensitive.
     * @param repository The name of the repository. The name is case-insensitive.
     * @return A [GHResult] containing a [Set] of [GHUser] objects representing the available assignees,
     * if the request is successful, or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.NotFound]: The repository does not exist or the resource is not found.
     */
    public suspend fun fetchIssueAssignees(owner: String, repository: String): GHResult<List<GHUser>> {
        return getWithConfig("$API_URL/repos/$owner/$repository/assignees")
    }

    /**
     * Fetches a list of commonly used licenses on GitHub.
     *
     * See [Licenses#get-all-commonly-used-licenses](https://docs.github.com/rest/licenses#get-all-commonly-used-licenses)
     *
     * @param featured An optional [Boolean] flag to include only featured licenses. If not specified, both featured and
     * non-featured licenses will be included.
     * @param perPage The number of results per page (max 100). Default value is 30.
     * @param page The page number of the results to fetch. Default value is 1.
     * @return A [GHResult] containing a [Set] of [GHLicense] objects representing the commonly used licenses,
     * if the request is successful.
     */
    public suspend fun fetchCommonlyUsedLicenses(
        featured: Boolean? = null,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHLicense>> = getWithConfig("$API_URL/licenses") {
        url.parameters.apply {
            featured?.let { append(ParameterConstants.featured, it.toString()) }
            append(ParameterConstants.perPage, perPage.toString())
            append(ParameterConstants.page, page.toString())
        }
    }

    /**
     * Fetches the information of a specific license by its identifier.
     *
     * See [Licenses#get-a-license](https://docs.github.com/rest/licenses#get-a-license)
     *
     * @param license The identifier of the license to fetch.
     * @return A [GHResult] containing a [GHLicense] object representing the requested license,
     * if the request is successful, or an [ApiException] in case of errors. Possible [ApiException] statuses include:
     * - [HttpStatusCode.Forbidden]: The request is forbidden due to rate limiting, missing authentication, or
     * insufficient permissions.
     * - [HttpStatusCode.NotFound]: The license does not exist or the resource is not found.
     */
    public suspend fun fetchLicense(license: String): GHResult<GHLicense> = getWithConfig("$API_URL/licenses/$license")

    /**
     * Fetches the license information for a specific repository.
     *
     * See [Licenses#get-the-license-for-a-repository](https://docs.github.com/rest/licenses#get-the-license-for-a-repository)
     *
     * This method returns the contents of the repository's license file, if one is detected.
     *
     * @param owner The account owner of the repository. The name is not case-sensitive.
     * @param repository The name of the repository. The name is not case-sensitive.
     * @return A [GHResult] containing a [GHLicense] object representing the license information for the repository,
     * if the request is successful.
     */
    public suspend fun fetchLicenseForRepository(owner: String, repository: String): GHResult<GHLicense> {
        return getWithConfig("$API_URL/repos/$owner/$repository/license")
    }

    public suspend fun createCommit(
        owner: String,
        repository: String,
        commitMessage: String,
        tree: GHTree
    ): GHResult<GHCommitResponse> = postWithConfig("$API_URL/repos/$owner/$repository/git/commits") {
        @Serializable
        class CommitBody(
            val message: String,
            val tree: String
        )
        setBody(CommitBody(commitMessage, tree.sha))
    }
    
    /**
     * Creates a tree in a repository.
     *
     * See [Trees#Create-a-tree](https://docs.github.com/rest/git/trees#create-a-tree)
     *
     * @param owner The account owner of the repository. The name is not case sensitive.
     * @param repository The name of the repository. The name is not case sensitive.
     * @param baseTreeSha The sha of the base tree.
     * @param treeItems A list of [GHTree.Item] objects specifying the tree structure.
     * @return A [GHResult] containing the created [GHTree] or an [ApiException] with the following possible statuses:
     * - [HttpStatusCode.Forbidden]: The operation is forbidden.
     * - [HttpStatusCode.NotFound]: The resource is not found.
     * - [HttpStatusCode.UnprocessableEntity]: Validation failed or the endpoint has been spammed.
     */
    public suspend fun createTree(
        owner: String,
        repository: String,
        baseTreeSha: String,
        treeItems: List<GHTree.Item>
    ): GHResult<GHTree> = postWithConfig("$API_URL/repos/$owner/$repository/git/trees") {
        @Serializable
        class TreeEntry(
            val path: String,
            val mode: GHTree.Item.Mode,
            val type: GHTree.Item.Type,
            val content: String?
        )
        @Serializable
        class Tree(
            val baseTree: String,
            val tree: List<TreeEntry>
        )
        setBody(Tree(baseTreeSha, treeItems.map { TreeEntry(it.path, it.mode, it.type, it.content) }))
    }

    public suspend fun fetchBranch(owner: String, repository: String, branchName: String): GHResult<GHBranch> {
        return getWithConfig("$API_URL/repos/$owner/$repository/branches/$branchName")
    }

    /**
     * Fetches a reference from a Git repository.
     *
     * See [Refs#Get-a-reference](https://docs.github.com/rest/git/refs#get-a-reference)
     *
     * @param owner The account owner of the repository. The name is case-insensitive.
     * @param repository The name of the repository. The name is case-insensitive.
     * @param reference The formatted reference string, e.g., heads/<branch name> for branches or tags/<tag name> for tags.
     * @return A [GHResult] containing the fetched [GHRef] or an [ApiException] with the following possible statuses:
     * - [HttpStatusCode.NotFound]: The resource is not found.
     */
    public suspend fun fetchReference(owner: String, repository: String, reference: String): GHResult<GHRef> {
        return getWithConfig("$API_URL/repos/$owner/$repository/git/refs/$reference")
    }

    public suspend fun fetchMatchingReferences(owner: String, repository: String, reference: String): GHResult<List<GHRef>> {
        return getWithConfig("$API_URL/repos/$owner/$repository/git/matching-refs/$reference")
    }

    public suspend fun createReference(
        owner: String,
        repository: String,
        ref: String,
        sha: String,
        key: String
    ): GHResult<GHRef> = postWithConfig("$API_URL/repos/$owner/$repository/git/refs") {
        @Serializable
        class CreateReferenceBody(
            val ref: String,
            val sha: String,
            val key: String
        )
        setBody(CreateReferenceBody(ref, sha, key))
    }

    public suspend fun updateReference(
        owner: String,
        repository: String,
        ref: String,
        sha: String,
        force: Boolean = false
    ): GHResult<GHRef> = patchWithConfig("$API_URL/repos/$owner/$repository/git/refs/$ref") {
        @Serializable
        class UpdateReferenceBody(
            val sha: String,
            val force: Boolean
        )
        setBody(UpdateReferenceBody(sha, force))
    }

    public suspend fun deleteReference(owner: String, repository: String, ref: String): GHResult<Unit> {
        return deleteWithConfig("$API_URL/repos/$owner/$repository/git/refs/$ref")
    }

    /**
     * Fetches all available GitIgnore templates.
     *
     * See [Gitignore#Get-all-gitignore-templates](https://docs.github.com/rest/gitignore#get-all-gitignore-templates)
     *
     * @return A [GHResult] containing a set of available GitIgnore templates (as [String])
     */
    public suspend fun fetchAllGitIgnoreTemplates(): GHResult<List<String>> {
        return getWithConfig("$API_URL/gitignore/templates")
    }

    /**
     * Fetches a GitIgnore template by its name.
     *
     * See [Gitignore#Get-a-gitignore-template](https://docs.github.com/rest/gitignore#get-a-gitignore-template)
     *
     * @param name The name of the GitIgnore template to fetch.
     * @return A [GHResult] containing the requested [GHGitIgnoreTemplate]
     */
    public suspend fun fetchGitIgnoreTemplate(name: String): GHResult<GHGitIgnoreTemplate> {
        return getWithConfig("$API_URL/gitignore/templates/$name")
    }

    public suspend fun renderMarkdownDocument(text: String): GHResult<String> = postWithConfig("$API_URL/markdown") {
        @Serializable
        class MarkdownDocumentBody(val text: String)
        setBody(MarkdownDocumentBody(text))
    }

    public suspend fun listReleases(
        owner: String,
        repository: String,
        perPage: Int = 30,
        page: Int = 1
    ): GHResult<List<GHRelease>> = getWithConfig("$API_URL/repos/$owner/$repository/releases") {
        url.parameters.apply {
            append(ParameterConstants.perPage, perPage.toString())
            append(ParameterConstants.page, page.toString())
        }
    }

    public suspend fun fetchLatestRelease(owner: String, repository: String): GHResult<GHRelease> {
        return getWithConfig("$API_URL/repos/$owner/$repository/releases/latest")
    }

    public suspend fun fetchRateLimit(): GHResult<GHRateLimitOverview> = getWithConfig("$API_URL/rate_limit")

    public suspend fun fetchGist(gistId: String): GHResult<GHGist> = getWithConfig("$API_URL/gists/$gistId")

    public suspend fun deleteGist(gistId: String): GHResult<Unit> = deleteWithConfig("$API_URL/gists/$gistId")

    override fun close() {
        client.close()
    }

    public companion object {
        public const val API_URL: String = "https://api.github.com"

        public fun create(engine: HttpClientEngine, init: GitHubBuilder.() -> Unit = {}): GitHub {
            return GitHubBuilder().apply(init).build(engine)
        }

        public fun create(engine: HttpClientEngineFactory<*>, init: GitHubBuilder.() -> Unit = {}): GitHub {
            return GitHubBuilder().apply(init).build(engine.create())
        }
    }
}

public val GHJson: ContentType get() = ContentType("application", "vnd.github+json")
