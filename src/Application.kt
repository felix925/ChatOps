package jp.making.felix

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.parametersOf
import io.ktor.jackson.jackson
import io.ktor.locations.Location
import io.ktor.locations.location
import io.ktor.locations.locations
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.coroutines.asCoroutineDispatcher
import org.apache.http.client.HttpClient
import java.util.concurrent.Executors

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    install(ContentNegotiation) {
        jackson {}
    }
//    install(Sessions) {
//        cookie<GitHubSession>("GitHubSession", SessionStorageMemory())
//    }
    install(Routing)
    install(Sessions)

    val TOKEN: String = System.getenv("APITOKEN")
    val APPID: String = System.getenv("CL_ID")
    val APPSEC: String = System.getenv("CL_SEC")
    var code: String = "curl https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo,workflow"
    val tokens: String = "curl -X POST -d \"code=\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    val commands: String = "curl -X POST -H \"Authorization: token ${TOKEN}\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i  https://api.github.com/repos/SoyBeansLab/daizu-ChatOps/dispatches"
    val exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4)

    val gitHubOAuth2Settings = listOf(
        OAuthServerSettings.OAuth2ServerSettings(
            name = "github",
            authorizeUrl = "https://github.com/login/oauth/authorize",
            accessTokenUrl = "https://github.com/login/oauth/access_token",
            clientId = APPID,
            clientSecret = APPSEC,
            defaultScopes = listOf("workflow")
        )
    ).associateBy { it.name }

    //@Location("/test") class LoginWithGitHub()






    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        post("/test{hoge}") {
            val comment = call.parameters["hoge"]
//            if (call.sessions.get<GitHubSession>() != null) {
//                LoginWithGitHub()
//            }
//            val repo = Repository()
//            val calls = CallApi(repo)
//            val command = Command(code)
//            val result = calls.CallTest(command)
//            call.respond(result)
            comment?.apply{
                val response = SlackResponse(
                    "in_channel",
                    "${comment}",
                    ""
                )
                call.respond(response)
            }
            val responses = SlackResponse(
                "in_channel",
                "failed",
                ""
            )
            call.respond(responses)
        }
        get("/result") {
            call.respondText("result")
        }
        get("/testresult") {

            call.respondText("testresult")
        }
    }
    /*fun Route.loginWithGitHub(client: HttpClient) {
        //location<loginWithGitHub> {
            // Authentication Featureをinstall
            install(Authentication) {
                // OAuth2認証
                oauth("GitHubOAuth") {
                    client
                    exec.asCoroutineDispatcher()
                    providerLookup = { gitHubOAuth2Settings[application.locations.resolve<GitHubSession>(loginWithGitHub()::class, this)].type }
                    urlProvider = { "http://localhost:8080${application.locations.href(location)}" }
                }
            }

            // 認証後(OAuth2フローでcallbackとして戻ってきてから)の処理)
            handle {  }
        //}
    }*/
}


