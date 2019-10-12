package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation){
        jackson {}
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        post("/test"){
            val testMessages = "This is response test"
            val attachement = SlackResponseAttachement(testMessages)
            val response = SlackResponse(
                "in_channel",
                "(๑╹ω╹๑ )",
                arrayOf(attachement)
            )
            val repo = Repository()
            val caller = CallApi(repo)
            call.respond(attachement)
        }
    }
}

