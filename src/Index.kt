package jp.making.felix

import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
fun Route.index() {
    get<Index> {
        call.respondText("HelloWorld")
    }
}
