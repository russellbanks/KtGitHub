package objects.sorts

enum class RepositorySort {
    NEWEST,
    OLDEST,
    STARGAZERS,
    WATCHERS;

    override fun toString() = name.lowercase()
}
