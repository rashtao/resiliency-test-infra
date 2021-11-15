package com.arangodb

import eu.rekawek.toxiproxy.Proxy
import org.mockserver.client.MockServerClient

data class Deployment(
    val id: String,
    val endpoints: Map<String, Endpoint>
)

data class Endpoint(
    val id: String,
    val port: Int,
    val proxy: Proxy,
    val mockClient: MockServerClient
) {
    fun removeToxic(name: String) {
        if (proxy.toxics().all.any { it.name == name }) {
            proxy.toxics().get(name).remove()
        }
    }
}
