package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.takeFrom
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.io.IOException
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation){
        jackson {}
    }
    val TOKEN:String = System.getenv("APITOKEN")
    val APPID:String = System.getenv("CL_ID")
    val APPSEC:String = System.getenv("CL_SEC")
    var code:String = "https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo"
    val tokens:String = "curl -X POST -d \"code=$code\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    val commands:String = "curl -X POST -H \"Authorization: token ${TOKEN}\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i  https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches"

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        get("/test") {
            val repo = Repository()
            val calls = CallApi(repo)
            val command = Command(tokens)
            val result = calls.CallTest(command)
            call.respond(result)
        }
        get("/result"){
            call.respondText("result")
        }
        get("/testresult"){
            call.respondText("testresult")
        }

    }
}


