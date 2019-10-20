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
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        jackson {}
    }
    install(Locations)
    @Location("/login/{type?}") class login(val type: String = "github")

//    val TOKEN: String = System.getenv("APITOKEN")
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")
//    var code: String = "curl https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo,workflow"
//    val tokens: String = "curl -X POST -d \"code=\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
//    val commands: String = "curl¥-X POST -H \"Authorization:token ${TOKEN}\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches"

    val loginProviders = listOf(
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = APPID,
            clientSecret = APPSEC
        )
    ).associateBy {it.name}

    install(Authentication) {
        oauth("gitHubOAuth") {
            client = HttpClient(Apache)
            providerLookup = { loginProviders[application.locations.resolve<login>(login::class, this).type] }
            urlProvider = { url(login(it.name)) }
        }
    }


    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        authenticate("gitHubOAuth") {
            location<login>() {
                param("error") {
                    handle {
                        call.respond("error")
                    }
                }

                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                    if (principal != null) {
                        call.respond(principal)
                    } else {
                        call.respond("failed")
                    }
                }
            }
        }
    }
}


