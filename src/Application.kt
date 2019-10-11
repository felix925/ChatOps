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
            val pb = ProcessBuilder("curl","-X POST -H \"Authorization: token 5eb6b6a7e957065f71f2108bcdf5210525742fd2\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i  https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches")
            pb.start()
            call.respond(response)
        }
    }
}

