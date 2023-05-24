package com.russellbanks.ktgithub.internal

import com.russellbanks.ktgithub.GitHub
import com.russellbanks.ktgithub.objects.GHLicense
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GHLicenseTests : FunSpec({
    lateinit var github: GitHub

    beforeTest {
        github = createGitHub(defaultMockEngine)
    }

    afterEach {
        github.close()
    }

    test("fetch mit license") {
        github.fetchLicense("mit").getOrThrow() shouldBe mitLicenseExpected
    }

    test("fetch commonly used licenses") {
        github.fetchCommonlyUsedLicenses().getOrThrow() shouldBe commonLicensesExpected
    }
}) {
    companion object {
        val mitLicenseResponse = """
            {
              "key": "mit",
              "name": "MIT License",
              "spdx_id": "MIT",
              "url": "https://api.github.com/licenses/mit",
              "node_id": "MDc6TGljZW5zZW1pdA==",
              "html_url": "http://choosealicense.com/licenses/mit/",
              "description": "A permissive license that is short and to the point. It lets people do anything with your code with proper attribution and without warranty.",
              "implementation": "Create a text file (typically named LICENSE or LICENSE.txt) in the root of your source code and copy the text of the license into the file. Replace [year] with the current year and [fullname] with the name (or names) of the copyright holders.",
              "permissions": [
                "commercial-use",
                "modifications",
                "distribution",
                "sublicense",
                "private-use"
              ],
              "conditions": [
                "include-copyright"
              ],
              "limitations": [
                "no-liability"
              ],
              "body": "\n\nThe MIT License (MIT)\n\nCopyright (c) [year] [fullname]\n\nPermission is hereby granted, free of charge, to any person obtaining a copy\nof this software and associated documentation files (the \"Software\"), to deal\nin the Software without restriction, including without limitation the rights\nto use, copy, modify, merge, publish, distribute, sublicense, and/or sell\ncopies of the Software, and to permit persons to whom the Software is\nfurnished to do so, subject to the following conditions:\n\nThe above copyright notice and this permission notice shall be included in all\ncopies or substantial portions of the Software.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\nIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\nFITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\nAUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\nOUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\nSOFTWARE.\n",
              "featured": true
            }
        """.trimIndent()

        val mitLicenseExpected = GHLicense(
            key = "mit",
            name = "MIT License",
            spdxId = "MIT",
            url = "https://api.github.com/licenses/mit",
            nodeId = "MDc6TGljZW5zZW1pdA==",
            htmlUrl = "http://choosealicense.com/licenses/mit/",
            description = "A permissive license that is short and to the point. It lets people do anything with your code with proper attribution and without warranty.",
            implementation = "Create a text file (typically named LICENSE or LICENSE.txt) in the root of your source code and copy the text of the license into the file. Replace [year] with the current year and [fullname] with the name (or names) of the copyright holders.",
            permissions = listOf("commercial-use", "modifications", "distribution", "sublicense", "private-use"),
            conditions = listOf("include-copyright"),
            limitations = listOf("no-liability"),
            _body = """
                
                
                The MIT License (MIT)

                Copyright (c) [year] [fullname]

                Permission is hereby granted, free of charge, to any person obtaining a copy
                of this software and associated documentation files (the "Software"), to deal
                in the Software without restriction, including without limitation the rights
                to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
                copies of the Software, and to permit persons to whom the Software is
                furnished to do so, subject to the following conditions:

                The above copyright notice and this permission notice shall be included in all
                copies or substantial portions of the Software.

                THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
                IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
                FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
                AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
                LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
                OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
                SOFTWARE.
                
            """.trimIndent(),
            featured = true
        )

        val commonLicensesResponse = """
            [
              {
                "key": "mit",
                "name": "MIT License",
                "spdx_id": "MIT",
                "url": "https://api.github.com/licenses/mit",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "lgpl-3.0",
                "name": "GNU Lesser General Public License v3.0",
                "spdx_id": "LGPL-3.0",
                "url": "https://api.github.com/licenses/lgpl-3.0",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "mpl-2.0",
                "name": "Mozilla Public License 2.0",
                "spdx_id": "MPL-2.0",
                "url": "https://api.github.com/licenses/mpl-2.0",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "agpl-3.0",
                "name": "GNU Affero General Public License v3.0",
                "spdx_id": "AGPL-3.0",
                "url": "https://api.github.com/licenses/agpl-3.0",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "unlicense",
                "name": "The Unlicense",
                "spdx_id": "Unlicense",
                "url": "https://api.github.com/licenses/unlicense",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "apache-2.0",
                "name": "Apache License 2.0",
                "spdx_id": "Apache-2.0",
                "url": "https://api.github.com/licenses/apache-2.0",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              },
              {
                "key": "gpl-3.0",
                "name": "GNU General Public License v3.0",
                "spdx_id": "GPL-3.0",
                "url": "https://api.github.com/licenses/gpl-3.0",
                "node_id": "MDc6TGljZW5zZW1pdA=="
              }
            ]
        """.trimIndent()

        val commonLicensesExpected = listOf(
            GHLicense(
                key = "mit",
                name = "MIT License",
                spdxId = "MIT",
                url = "https://api.github.com/licenses/mit",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            ),
            GHLicense(
                key = "lgpl-3.0",
                name = "GNU Lesser General Public License v3.0",
                spdxId ="LGPL-3.0",
                url = "https://api.github.com/licenses/lgpl-3.0",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            ),
            GHLicense(
                key = "mpl-2.0",
                name = "Mozilla Public License 2.0",
                spdxId = "MPL-2.0",
                url = "https://api.github.com/licenses/mpl-2.0",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            ),
            GHLicense(
                key = "agpl-3.0",
                name = "GNU Affero General Public License v3.0",
                spdxId = "AGPL-3.0",
                url = "https://api.github.com/licenses/agpl-3.0",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured=null
            ),
            GHLicense(
                key = "unlicense",
                name = "The Unlicense",
                spdxId = "Unlicense",
                url = "https://api.github.com/licenses/unlicense",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            ),
            GHLicense(
                key = "apache-2.0",
                name = "Apache License 2.0",
                spdxId = "Apache-2.0",
                url = "https://api.github.com/licenses/apache-2.0",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation=  null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            ),
            GHLicense(
                key = "gpl-3.0",
                name = "GNU General Public License v3.0",
                spdxId = "GPL-3.0",
                url = "https://api.github.com/licenses/gpl-3.0",
                nodeId = "MDc6TGljZW5zZW1pdA==",
                htmlUrl = null,
                description = null,
                implementation = null,
                permissions = null,
                conditions = null,
                limitations = null,
                _body = null,
                featured = null
            )
        )
    }
}
