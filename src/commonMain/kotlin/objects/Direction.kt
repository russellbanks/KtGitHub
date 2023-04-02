package objects

public enum class Direction {
    ASCENDING { override fun toString(): String = name.take(3).lowercase() },
    DESCENDING { override fun toString(): String = name.take(4).lowercase() };

    public companion object {
        internal const val name = "direction"
    }
}
