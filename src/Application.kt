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
import io.ktor.routing.route
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
//        get("/test") {
//            val repo = Repository()
//            val caller = CallApi(repo)
//            call.respond(caller.accessCode())
//        }
        route("/test") {
            route("/") {
                get {
                    val code: String? = call.parameters["code"]
                    code?.apply {
                        call.respond(this + "success")
                    }
                    if(code == null)
                        call.respondText("/?code= failed")
                }
            }
            get{
                call.respondText("/test routing")
            }
        }
        get("/testresult"){
            val repo = Repository()
            val caller = CallApi(repo)
            call.respond(caller.CallTest())
        }
    }
}

