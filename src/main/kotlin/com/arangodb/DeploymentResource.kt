package com.arangodb

import org.eclipse.microprofile.openapi.annotations.Operation
import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.PathParam

class DeploymentResource(private val d: Deployment) {

    @GET
    fun get() = DeploymentDto(d)

    @Path("/{endpointId}")
    @Operation(summary = "Get endpoint")
    fun getEndpoint(@PathParam("endpointId") id: String) =
        EndpointResource(d.endpoints[id] ?: throw NotFoundException())

}