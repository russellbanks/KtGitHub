package objects.sorts

enum class SinceSort {
    CREATED,
    UPDATED;

    override fun toString() = name.lowercase()
}
