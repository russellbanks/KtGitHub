package objects

enum class Direction {
    ASCENDING { override fun toString() = name.take(3).lowercase() },
    DESCENDING { override fun toString() = name.take(4).lowercase() };

    companion object {
        internal const val name = "direction"
    }
}
