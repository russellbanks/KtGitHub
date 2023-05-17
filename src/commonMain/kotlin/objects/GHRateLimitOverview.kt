package objects

import kotlinx.serialization.Serializable

@Serializable
public class GHRateLimitOverview(
    public val resources: Resources,
    public val rate: RateLimit
) {
    @Serializable
    public class Resources(
        public val core: RateLimit,
        public val graphql: RateLimit?,
        public val search: RateLimit,
        public val sourceImport: RateLimit?,
        public val integrationManifest: RateLimit?,
        public val codeScanningUpload: RateLimit?,
        public val actionsRunnerRegistration: RateLimit?,
        public val scim: RateLimit?,
        public val dependencySnapshots: RateLimit?
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Resources

            if (core != other.core) return false
            if (graphql != other.graphql) return false
            if (search != other.search) return false
            if (sourceImport != other.sourceImport) return false
            if (integrationManifest != other.integrationManifest) return false
            if (codeScanningUpload != other.codeScanningUpload) return false
            if (actionsRunnerRegistration != other.actionsRunnerRegistration) return false
            if (scim != other.scim) return false
            return dependencySnapshots == other.dependencySnapshots
        }

        override fun hashCode(): Int {
            var result = core.hashCode()
            result = 31 * result + (graphql?.hashCode() ?: 0)
            result = 31 * result + search.hashCode()
            result = 31 * result + (sourceImport?.hashCode() ?: 0)
            result = 31 * result + (integrationManifest?.hashCode() ?: 0)
            result = 31 * result + (codeScanningUpload?.hashCode() ?: 0)
            result = 31 * result + (actionsRunnerRegistration?.hashCode() ?: 0)
            result = 31 * result + (scim?.hashCode() ?: 0)
            result = 31 * result + (dependencySnapshots?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Resources(core=$core, graphql=$graphql, search=$search, sourceImport=$sourceImport, integrationManifest=$integrationManifest, codeScanningUpload=$codeScanningUpload, actionsRunnerRegistration=$actionsRunnerRegistration, scim=$scim, dependencySnapshots=$dependencySnapshots)"
        }
    }

    @Serializable
    public class RateLimit(
        public val limit: Int,
        public val remaining: Int,
        public val reset: Int,
        public val used: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as RateLimit

            if (limit != other.limit) return false
            if (remaining != other.remaining) return false
            if (reset != other.reset) return false
            return used == other.used
        }

        override fun hashCode(): Int {
            var result = limit
            result = 31 * result + remaining
            result = 31 * result + reset
            result = 31 * result + used
            return result
        }

        override fun toString(): String {
            return "RateLimit(limit=$limit, remaining=$remaining, reset=$reset, used=$used)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHRateLimitOverview

        if (resources != other.resources) return false
        return rate == other.rate
    }

    override fun hashCode(): Int {
        var result = resources.hashCode()
        result = 31 * result + rate.hashCode()
        return result
    }

    override fun toString(): String {
        return "GHRateLimitOverview(resources=$resources, rate=$rate)"
    }
}
