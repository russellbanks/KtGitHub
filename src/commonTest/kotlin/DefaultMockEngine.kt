import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

val defaultMockEngine = MockEngine { request ->
    val pathSegments = request.url.pathSegments.filterNot(String::isEmpty)
    if (!request.url.toString().startsWith(GitHub.apiUrl)) respondBadRequest()
    when {
        pathSegments.first() == "repos" -> when {
            pathSegments.takeLast(2) == listOf("octocat", "Hello-World") -> when (request.method) {
                HttpMethod.Get -> respondGHOk(GHRepositoryTests.response)
                HttpMethod.Delete -> respondDeleted()
                else -> respondBadRequest()
            }
            pathSegments.takeLast(2) == listOf("forbidden", "forbidden-repository") -> respondForbidden()
            pathSegments.takeLast(2).first() == "branches" -> respondGHOk(GHBranchTests.response)
            pathSegments.takeLast(2) == listOf("git", "trees") -> respondGHOk(GHTreeTests.response)
            else -> respondError(HttpStatusCode.NotFound)
        }
        pathSegments == listOf("users", "octocat") -> respondGHOk(GHUserTests.response)
        pathSegments.take(2) == listOf("gitignore", "templates") -> {
            if (pathSegments.size == 3 && pathSegments.last() == "C") {
                respondGHOk(GHGitIgnoreTemplateTests.cTemplateResponse)
            } else {
                respondGHOk(GHGitIgnoreTemplateTests.allTemplatesResponse)
            }
        }
        pathSegments.first() == "licenses" -> {
            if (pathSegments.take(2) == listOf("licenses", "mit")) {
                respondGHOk(GHLicenseTests.mitLicenseResponse)
            } else {
                respondGHOk(GHLicenseTests.commonLicensesResponse)
            }
        }
        else -> respondError(HttpStatusCode.NotFound)
    }
}

fun MockRequestHandleScope.respondGHOk(content: String) = respond(
    content = content,
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, GHJson.toString())
)

fun MockRequestHandleScope.respondDeleted() = respond(
    content = ByteReadChannel.Empty,
    status = HttpStatusCode.NoContent,
    headers = headersOf(HttpHeaders.ContentType, GHJson.toString())
)

fun MockRequestHandleScope.respondForbidden() = respond(
    content = ByteReadChannel.Empty,
    status = HttpStatusCode.Forbidden,
    headers = headersOf(HttpHeaders.ContentType, GHJson.toString())
)

fun createGitHub(mockEngine: MockEngine): GitHub = GitHub.create(mockEngine)
