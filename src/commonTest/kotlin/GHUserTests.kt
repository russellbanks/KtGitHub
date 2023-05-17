import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import objects.GHUser

class GHUserTests : FunSpec({
    lateinit var github: GitHub

    beforeTest {
        github = createGitHub(defaultMockEngine)
    }

    afterEach {
        github.close()
    }

    test("fetch user") {
        github.fetchUser("octocat").getOrThrow() shouldBe expected
    }
}) {
    companion object {
        val response = """
            {
              "login": "octocat",
              "id": 1,
              "node_id": "MDQ6VXNlcjE=",
              "avatar_url": "https://github.com/images/error/octocat_happy.gif",
              "gravatar_id": "",
              "url": "https://api.github.com/users/octocat",
              "html_url": "https://github.com/octocat",
              "followers_url": "https://api.github.com/users/octocat/followers",
              "following_url": "https://api.github.com/users/octocat/following{/other_user}",
              "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
              "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
              "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
              "organizations_url": "https://api.github.com/users/octocat/orgs",
              "repos_url": "https://api.github.com/users/octocat/repos",
              "events_url": "https://api.github.com/users/octocat/events{/privacy}",
              "received_events_url": "https://api.github.com/users/octocat/received_events",
              "type": "User",
              "site_admin": false,
              "name": "monalisa octocat",
              "company": "GitHub",
              "blog": "https://github.com/blog",
              "location": "San Francisco",
              "email": "octocat@github.com",
              "hireable": false,
              "bio": "There once was...",
              "twitter_username": "monatheoctocat",
              "public_repos": 2,
              "public_gists": 1,
              "followers": 20,
              "following": 0,
              "created_at": "2008-01-14T04:33:35Z",
              "updated_at": "2008-01-14T04:33:35Z"
            }
        """.trimIndent()

        val expected = GHUser(
            name = "monalisa octocat",
            email = "octocat@github.com",
            login = "octocat",
            id = 1,
            htmlUrl = "https://github.com/octocat",
            avatarUrl = "https://github.com/images/error/octocat_happy.gif",
            followersUrl = "https://api.github.com/users/octocat/followers",
            followingUrl = "https://api.github.com/users/octocat/following{/other_user}",
            gistsUrl = "https://api.github.com/users/octocat/gists{/gist_id}",
            starredUrl = "https://api.github.com/users/octocat/starred{/owner}{/repo}",
            subscriptionsUrl = "https://api.github.com/users/octocat/subscriptions",
            organizationsUrl = "https://api.github.com/users/octocat/orgs",
            reposUrl = "https://api.github.com/users/octocat/repos",
            eventsUrl = "https://api.github.com/users/octocat/events{/privacy}",
            receivedEventsUrl = "https://api.github.com/users/octocat/received_events",
            type = "User",
            sideAdmin = null,
            url = "https://api.github.com/users/octocat",
            company = "GitHub",
            blogUrl = "https://github.com/blog",
            location = "San Francisco",
            isHireable=false,
            bio = "There once was...",
            twitterUsername = "monatheoctocat",
            publicRepositoriesCount = 2,
            publicGistsCount = 1,
            followersCount = 20,
            followingCount = 0,
            createdAt = LocalDateTime(2008, 1, 14, 4, 33, 35).toInstant(TimeZone.UTC),
            updatedAt = LocalDateTime(2008, 1, 14, 4, 33, 35).toInstant(TimeZone.UTC),
            privateGistsCount=null
        )
    }
}