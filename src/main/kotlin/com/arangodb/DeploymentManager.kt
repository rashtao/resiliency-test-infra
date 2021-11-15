package com.arangodb

import eu.rekawek.toxiproxy.ToxiproxyClient
import org.mockserver.integration.ClientAndServer
import javax.inject.Singleton

@Singleton
class DeploymentManager {

    private val client = ToxiproxyClient("127.0.0.1", 8474)
    val deployments = mapOf(
        "single" to Deployment(
            "single", mapOf(
                "18529" to createEndpoint("single", 18529, "172.28.13.1")
            )
        ),
        "cluster" to Deployment(
            "cluster", mapOf(
                "28529" to createEndpoint("cluster", 28529, "172.28.23.1"),
                "28539" to createEndpoint("cluster", 28539, "172.28.23.2"),
                "28549" to createEndpoint("cluster", 28549, "172.28.23.3"),
            )
        ),
        "activefailover" to Deployment(
            "activefailover", mapOf(
                "38529" to createEndpoint("activefailover", 38529, "172.28.33.1"),
                "38539" to createEndpoint("activefailover", 38539, "172.28.33.2"),
                "38549" to createEndpoint("activefailover", 38549, "172.28.33.3"),
            )
        ),
    )

    private fun createEndpoint(deploymentId: String, port: Int, upHost: String, upPort: Int = 8529): Endpoint {
        val proxyId = "$deploymentId-$port"
        val mockClient = ClientAndServer.startClientAndServer(upHost, upPort)

        // delete proxy if it exists already
        if (client.proxies.any { it.name == proxyId })
            client.getProxy(proxyId).delete()

        val proxy = client.createProxy(proxyId, "127.0.0.1:$port", "127.0.0.1:${mockClient.port}")
        return Endpoint("$port", port, proxy, mockClient)
    }

}