package com.russellbanks.ktgithub.objects

import com.russellbanks.ktgithub.GHResult
import com.russellbanks.ktgithub.GitHub
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GHLicense internal constructor(
    public var key: String,
    public var name: String,
    public var spdxId: String?,
    public var url: String?,
    public var nodeId: String,
    public var htmlUrl: String? = null,
    public var description: String? = null,
    public var implementation: String? = null,
    public var permissions: List<String>? = null,
    public var conditions: List<String>? = null,
    public var limitations: List<String>? = null,
    @SerialName("body") private var _body: String? = null,
    public var featured: Boolean? = null
) : GitHubObject() {
    /**
     * Returns the body, or fetches the full license data if the body is null (such as from retrieving all common
     * licenses that don't have the full license data).
     */
    public suspend fun fetchBody(): GHResult<String> = if (_body == null) {
        getFullLicenseData().map { it._body as String }
    } else {
        GHResult(Result.success(_body as String))
    }

    private suspend fun getFullLicenseData(): GHResult<GHLicense> {
        return getWithConfig<GHLicense>("${GitHub.apiUrl}/$name").onSuccess {
            key = it.key
            name = it.name
            spdxId = it.spdxId
            url = it.url
            nodeId = it.nodeId
            htmlUrl = it.htmlUrl
            description = it.description
            implementation = it.implementation
            permissions = it.permissions
            conditions = it.conditions
            limitations = it.limitations
            _body = it._body
            featured = it.featured
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GHLicense

        if (key != other.key) return false
        if (name != other.name) return false
        if (spdxId != other.spdxId) return false
        if (url != other.url) return false
        if (nodeId != other.nodeId) return false
        if (htmlUrl != other.htmlUrl) return false
        if (description != other.description) return false
        if (implementation != other.implementation) return false
        if (permissions != other.permissions) return false
        if (conditions != other.conditions) return false
        if (limitations != other.limitations) return false
        if (_body != other._body) return false
        return featured == other.featured
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (spdxId?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + nodeId.hashCode()
        result = 31 * result + (htmlUrl?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (implementation?.hashCode() ?: 0)
        result = 31 * result + (permissions?.hashCode() ?: 0)
        result = 31 * result + (conditions?.hashCode() ?: 0)
        result = 31 * result + (limitations?.hashCode() ?: 0)
        result = 31 * result + (_body?.hashCode() ?: 0)
        result = 31 * result + (featured?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "GHLicense(key='$key', name='$name', spdxId=$spdxId, url=$url, nodeId='$nodeId', htmlUrl=$htmlUrl, description=$description, implementation=$implementation, permissions=$permissions, conditions=$conditions, limitations=$limitations, _body=$_body, featured=$featured)"
    }
}
