package objects.sorts

public enum class RepositorySort {
    NEWEST,
    OLDEST,
    STARGAZERS,
    WATCHERS;

    override fun toString(): String = name.lowercase()
}
