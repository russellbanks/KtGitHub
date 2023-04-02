package objects.sorts

public enum class SinceSort {
    CREATED,
    UPDATED;

    override fun toString(): String = name.lowercase()
}
