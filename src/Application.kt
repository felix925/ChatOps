package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation){
        jackson {}
    }
    install(Sessions) {
        cookie<GitHubSession>("GitHubSession", SessionStorageMemory())
    }
    install(Routing)
    install(Sessions)

    val TOKEN:String = System.getenv("APITOKEN")
    val APPID:String = System.getenv("CL_ID")
    val APPSEC:String = System.getenv("CL_SEC")
    var code:String = "https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo"
    val token:String = "curl -X POST -d \"code=$code\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    val command = "curl¥ REST ¥-H ¥\"Accept: application/vnd.github.everest-preview+json\" ¥-d ¥'{\"event_type\":\"custom.preview\"}' ¥-i ¥https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches?access_token=${TOKEN}"

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        get("/test") {
            call.respondRedirect(code)
        }
        get("/result"){
        }
        get("/testresult"){
        }
    }
}


