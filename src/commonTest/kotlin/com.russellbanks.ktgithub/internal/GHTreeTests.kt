package com.russellbanks.ktgithub.internal

import com.russellbanks.ktgithub.GitHub
import com.russellbanks.ktgithub.objects.GHTree
import io.kotest.core.spec.style.FunSpec

class GHTreeTests : FunSpec({
    lateinit var github: GitHub

    beforeTest {
        github = createGitHub(defaultMockEngine)
    }

    afterEach {
        github.close()
    }

    test("fetch github branch") {
        val repository = github.fetchRepository("octocat", "Hello-World").getOrThrow()
        val branch = repository.fetchBranch(repository.defaultBranch).getOrThrow()
        repository.createTree(
            branch,
            listOf(GHTree.Item("file.kt", "Some text", false))
        ).getOrThrow()
    }
}) {
    companion object {
        val response = """
            {
              "sha": "cd8274d15fa3ae2ab983129fb037999f264ba9a7",
              "url": "https://api.github.com/repos/octocat/Hello-World/trees/cd8274d15fa3ae2ab983129fb037999f264ba9a7",
              "tree": [
                {
                  "path": "file.rb",
                  "mode": "100644",
                  "type": "blob",
                  "size": 132,
                  "sha": "7c258a9869f33c1e1e1f74fbb32f07c86cb5a75b",
                  "url": "https://api.github.com/repos/octocat/Hello-World/git/blobs/7c258a9869f33c1e1e1f74fbb32f07c86cb5a75b"
                }
              ],
              "truncated": true
            }
        """.trimIndent()
    }
}
