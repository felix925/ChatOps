package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.locations.Location
import io.ktor.locations.location
import io.ktor.locations.locations
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import kotlinx.coroutines.asCoroutineDispatcher
import org.apache.http.client.HttpClient
import java.util.concurrent.Executors

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Location("/test") class LoginWithGitHub()

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation){
        jackson {}
    }
    install(Sessions){
        cookie<GitHubSession>("GitHubSession",SessionStorageMemory())
    }
    install(Routing)

    val TOKEN:String = System.getenv("APITOKEN")
    val APPID:String = System.getenv("CL_ID")
    val APPSEC:String = System.getenv("CL_SEC")
    var code:String = "curl https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo,workflow"
    val tokens:String = "curl -X POST -d \"code=$code\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    val commands:String = "curl -X POST -H \"Authorization: token ${TOKEN}\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i  https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches"
    val exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4)
    val gitHubOAuth2Settings = OAuthServerSettings.OAuth2ServerSettings(
        name = "github",
        authorizeUrl = "https://github.com/login/oauth/authorize",
        accessTokenUrl = "https://github.com/login/oauth/access_token",
        clientId = APPID,
        clientSecret = APPSEC,
        defaultScopes = listOf("workflow")
    )

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
//        get("/test") {
//            val repo = Repository()
//            val calls = CallApi(repo)
//            val command = Command(code)
//            val result = calls.CallTest(command)
//            call.respond(result)
//        }
        get("/result"){
            call.respondText("result")
        }
        get("/testresult"){
            call.respondText("testresult")
        }
    }
    fun Route.loginWithGitHub(client: HttpClient) {
        location<LoginWithGitHub> {
            // Authentication Featureをinstall
            install(Authentication) {
                // OAuth2認証
                oauth("LoginWithGitHub") {
                    client
                    exec.asCoroutineDispatcher()
                    providerLookup = { gitHubOAuth2Settings }
                    urlProvider = { "https://felixops.herokuapp.com" }
                }
            }

            // 認証後(OAuth2フローでcallbackとして戻ってきてから)の処理)
            handle {
                call.principal<OAuthAccessTokenResponse.OAuth2>()?.apply {
                    call.respond(this)
                }
                call.respondText("failed")
            }
        }
    }
}


