package com.arangodb

import org.eclipse.microprofile.openapi.annotations.Operation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Path("/deployment")
@Produces(MediaType.APPLICATION_JSON)
class DeploymentsResource(private val dm: DeploymentManager) {

    @GET
    @Operation(summary = "Get all deployments")
    fun getAll() = dm.deployments.values.map(::DeploymentDto)

    @Path("/{deploymentId}")
    @Operation(summary = "Get deployment")
    fun getDeployment(@PathParam("deploymentId") id: String) =
        DeploymentResource(dm.deployments[id] ?: throw NotFoundException())

}
