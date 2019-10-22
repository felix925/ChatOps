package jp.making.felix

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.FreeMarker
import io.ktor.jackson.jackson
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.util.concurrent.Executors

@Location("/") class Index()
@Location("/test") class login(val type:String? = "github")

data class GitHubSession(val accessToken: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")
    val loginProviders = listOf(
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = APPID,
            clientSecret = APPSEC
        )
    ).associateBy {it.name}
    val client = HttpClient(Apache)
    val authOauthForLogin = "authOauthForLogin"
    install(ContentNegotiation) {
        jackson {}
    }
    environment.monitor.subscribe(ApplicationStopping) {
        client.close()
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(Sessions) {
        cookie<GitHubSession>("GitHubSession", SessionStorageMemory())
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(Application::class.java.classLoader, "templates")
    }
    install(Authentication) {
        oauth("gitHubOAuth") {
            client
            providerLookup = { loginProviders[application.locations.resolve<login>(login::class, this).type] }
            urlProvider = { url(login(it.name)) }
        }
    }
    install(Routing) {
        index(client)
        authenticate(authOauthForLogin) {
            location<login>() {
                param("error") {
                    handle {
                        call.respondText { "failed" }
                    }
                }

                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                    if (principal != null) {
                        call.respond(principal)
                    } else {
                        call.respondText { "in handle but failed" }
                    }
                }
            }
        }
    }
//    val TOKEN: String = System.getenv("APITOKEN")
//    val APPID: String = System.getenv("CL_ID")
//    val APPSEC: String = System.getenv("CL_SEC")
}
