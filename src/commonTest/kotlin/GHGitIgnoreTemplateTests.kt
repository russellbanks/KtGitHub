
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import objects.GHGitIgnoreTemplate

class GHGitIgnoreTemplateTests : FunSpec({
    lateinit var github: GitHub

    beforeTest {
        github = createGitHub(defaultMockEngine)
    }

    afterEach {
        github.close()
    }

    test("fetch all gitignore templates") {
        github.fetchAllGitIgnoreTemplates().getOrNull() shouldBe allTemplatesExpected
    }

    test("fetch a gitignore template") {
        github.fetchGitIgnoreTemplate("C").getOrThrow() shouldBe cTemplateExpected
    }

}) {
    companion object {
        val allTemplatesResponse = """
            [
              "Actionscript",
              "Android",
              "AppceleratorTitanium",
              "Autotools",
              "Bancha",
              "C",
              "C++"
            ]
        """.trimIndent()

        val allTemplatesExpected = listOf(
            "Actionscript", "Android", "AppceleratorTitanium", "Autotools", "Bancha", "C", "C++"
        )

        val cTemplateResponse = """
            {
              "name": "C",
              "source": "# Object files\n*.o\n\n# Libraries\n*.lib\n*.a\n\n# Shared objects (inc. Windows DLLs)\n*.dll\n*.so\n*.so.*\n*.dylib\n\n# Executables\n*.exe\n*.out\n*.app\n"
            }
        """.trimIndent()

        val cTemplateExpected = GHGitIgnoreTemplate(
            name = "C",
            source = """
                # Object files
                *.o
                
                # Libraries
                *.lib
                *.a
                
                # Shared objects (inc. Windows DLLs)
                *.dll
                *.so
                *.so.*
                *.dylib
                
                # Executables
                *.exe
                *.out
                *.app
                
            """.trimIndent()
        )
    }
}
