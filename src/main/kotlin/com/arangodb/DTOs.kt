package com.arangodb

import eu.rekawek.toxiproxy.model.ToxicDirection
import org.eclipse.microprofile.openapi.annotations.media.Schema

data class DeploymentDto(
    val id: String,
    val endpoints: List<EndpointDto>
) {
    constructor(d: Deployment) : this(d.id, d.endpoints.values.map(::EndpointDto))
}

data class EndpointDto(
    val id: String,
    val address: String
) {
    constructor(ep: Endpoint) : this(ep.id, "127.0.0.1:${ep.port}")
}

data class LatencyToxic(
    @field:Schema(required = true, example = "DOWNSTREAM")
    val direction: ToxicDirection,
    @field:Schema(required = true, description = "ms", example = "5000")
    val latency: Long
)

data class MockRequest(
    @field:Schema(
        required = false,
        description = "The HTTP method to match on such as \"GET\" or \"POST\"",
        example = "GET"
    )
    val method: String?,
    @field:Schema(
        required = true,
        description = """
            The path to match on such as "/some_mocked_path" any servlet context path is ignored for matching and should
            not be specified here regex values are also supported such as ".*_path", see 
            http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html for full details of the supported 
            regex syntax""",
        example = "/_api/.*"
    )
    val path: String,
    @field:Schema(
        required = true,
        description = """
        The status code to return, such as 200, 404, the status code specified here will result in the default status 
        message for this status code for example for 200 the status message "OK" is used""",
        example = "503"
    )
    val statusCode: Int,
    @field:Schema(
        required = true,
        description = "Set response body to return as a string response body. It must be a valid JSON string.",
        example = """{"message":"boom"}"""
    )
    val body: String
)
