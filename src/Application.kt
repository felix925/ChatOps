package jp.making.felix

import com.sun.tools.javac.comp.Env
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
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import kotlinx.css.i
import java.nio.charset.Charset
import java.io.InputStreamReader
import java.io.BufferedReader



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
            call.respond(response)
        }
        get("/token"){
            call.respond(System.getenv("TOKEN"))
        }
    }
}

