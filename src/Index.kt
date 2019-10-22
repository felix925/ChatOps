package jp.making.felix

import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.Route

fun Route.index(client: HttpClient) {
    get<Index> {
        call.respondText("HelloWorld")
    }
}
