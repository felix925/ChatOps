package jp.making.felix

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
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
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.io.IOException
import java.util.concurrent.TimeUnit

@KtorExperimentalLocationsAPI
@Location("/") class Index()
@KtorExperimentalLocationsAPI
@Location("/test") class login(val type:String? = "github")

data class GitHubSession(val accessToken: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")
    val loginProviders =
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = APPID,
            clientSecret = APPSEC,
            defaultScopes = listOf("workflow","repo","repo:status","repo_deployment","public_repo","admin:org","admin:org_hookS")
    )
    install(ContentNegotiation) {
        jackson {}
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
            client = HttpClient(Apache)
            providerLookup = { loginProviders }
            urlProvider = {"https://felixops.herokuapp.com/test"}
            //urlProvider = { url(login(it.name)) }
        }
    }
    install(Routing) {
        index()
        authenticate("gitHubOAuth") {
            location<login>{
                param("error") {
                    handle {
                        call.respondText { "failed" }
                    }
                }

                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse>()
                    var token = principal.toString()
                    token = token.replace("OAuth2(accessToken=","")
                    token = token.split(",")[0]
//                    val token = call.parameters["code"]
                    token.apply {
                        val calls: CallApi = CallApi(token)
                        val result: String = calls.Calls()
                        call.respond(result)
                    }
                    call.respondText { "token is null" }
                }

            }
        }
    }

//    val TOKEN: String = System.getenv("APITOKEN")
//    val APPID: String = System.getenv("CL_ID")
//    val APPSEC: String = System.getenv("CL_SEC")
}
