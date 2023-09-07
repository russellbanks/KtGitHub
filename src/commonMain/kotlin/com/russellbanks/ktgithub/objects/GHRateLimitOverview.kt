package com.russellbanks.ktgithub.objects

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

@Serializable
@Poko public class GHRateLimitOverview(
    public val resources: Resources,
    public val rate: RateLimit
) {
    @Serializable
    @Poko public class Resources(
        public val core: RateLimit,
        public val graphql: RateLimit?,
        public val search: RateLimit,
        public val sourceImport: RateLimit?,
        public val integrationManifest: RateLimit?,
        public val codeScanningUpload: RateLimit?,
        public val actionsRunnerRegistration: RateLimit?,
        public val scim: RateLimit?,
        public val dependencySnapshots: RateLimit?
    )

    @Serializable
    @Poko public class RateLimit(
        public val limit: Int,
        public val remaining: Int,
        public val reset: Int,
        public val used: Int
    )
}
