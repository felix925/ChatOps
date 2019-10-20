package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.routing.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        jackson {}
    }
    install(Routing)
    install(Locations)
    val TOKEN: String = System.getenv("APITOKEN")
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")



    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        @Location("/test")
            data class Message(val message: String?)
            get<Message> {param->
                param.message?.apply {
                    call.respond(param.message)
                }
                call.respond("failed")

            }
    }
}