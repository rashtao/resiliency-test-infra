package com.arangodb

import eu.rekawek.toxiproxy.model.Toxic
import org.eclipse.microprofile.openapi.annotations.Operation
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path

class EndpointResource(private val ep: Endpoint) {

    @GET
    fun get() = EndpointDto(ep)

    @POST
    @Path("/reset")
    @Operation(
        summary = "Reset endpoint",
        description = "Enable the proxy, remove all the toxics and remove all the mocks"
    )
    fun reset() {
        ep.proxy.enable()
        ep.proxy.toxics().all.forEach(Toxic::remove)
        ep.mockClient.reset()
    }

    @POST
    @Path("/enable")
    @Operation(summary = "Enable endpoint", description = "Activate the proxy for the endpoint")
    fun enable() {
        ep.proxy.enable()
    }

    @POST
    @Path("/disable")
    @Operation(summary = "Disable endpoint", description = "Disable the proxy for the endpoint")
    fun disable() {
        ep.proxy.disable()
    }

    @POST
    @Path("/latency")
    @Operation(summary = "Add latency", description = "Add a delay to all data going through the endpoint proxy")
    fun latency(t: LatencyToxic) {
        ep.removeToxic("latency")
        ep.proxy.toxics().latency("latency", t.direction, t.latency)
    }

    @POST
    @Path("/resetPeer")
    @Operation(
        summary = "Connection reset by peer",
        description = "Simulate a TCP RESET on the new connections"
    )
    fun resetPeer(t: ResetPeerToxic) {
        ep.removeToxic("resetPeer")
        ep.proxy.toxics().resetPeer("resetPeer", t.direction, t.timeout)
    }

    @POST
    @Path("/mock")
    @Operation(summary = "Add mock", description = "Add a mock response to the endpoint")
    fun mock(mr: MockRequest) {
        ep.mockClient
            .`when`(
                HttpRequest.request()
                    .withMethod(mr.method)
                    .withPath(mr.path)
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(mr.statusCode)
                    .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                    .withBody(mr.body, Charsets.UTF_8)
            )
    }

}