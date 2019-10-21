package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.locations.location
import io.ktor.locations.locations
import io.ktor.locations.url
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.routing.get
import java.util.concurrent.Executors

data class SlackResponse(
    val response_type: String,
    val text: String
)
data class login(val type:String = "github")

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        jackson {}
    }
    install(Locations)
    val TOKEN: String = System.getenv("APITOKEN")
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")
    val exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4)
    val loginProvider = listOf(
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = "${APPID}",
            clientSecret = "${APPSEC}"
        )
    ).associateBy { it.name }
    install(Authentication) {
        oauth("gitHubOAuth") {
            client = HttpClient(Apache)
            providerLookup = { loginProvider[application.locations.resolve<login>(login::class, this).type] }
            urlProvider = { url(login(it.name)) }
        }
    }


    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/test") {
            val comment = call.receiveText().split("text=")
            val text:String = comment[1].split("&")[0]
            val res = SlackResponse("in_channel","${text}を受け取りました！")


            call.respond(res)
        }
        authenticate("gitHubOAuth") {
            location<login>() {
                param("error") {
                    handle {
                        call.respond(call.parameters.getAll("error").orEmpty())
                    }
                }

                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                    if (principal != null) {
                        call.respond(principal)
                    } else {
                        call.respondText("failed")
                    }
                }
            }
        }
    }
}