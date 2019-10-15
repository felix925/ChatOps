package jp.making.felix

import java.io.IOException
import java.util.concurrent.TimeUnit


data class Repository(val value: String = "daizu-ChatOps")

class CallApi(repository: Repository){
    //private val TOKEN:String = System.getenv("TOKEN")
    private val TOKEN:String = System.getenv("APITOKEN")
    private val APPID:String = System.getenv("CL_ID")
    private val APPSEC:String = System.getenv("CL_SEC")
    private var code:String = "https://github.com/login/oauth/authorize?client_id=$APPID&scope=repo"
    private val token:String = "curl -X POST -d \"code=$code\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    private val command = "curl¥ REST ¥-H ¥\"Accept: application/vnd.github.everest-preview+json\" ¥-d ¥'{\"event_type\":\"custom.preview\"}' ¥-i ¥https://api.github.com/repos/SoyBeansLab/${repository.value}/dispatches?access_token=${TOKEN}"
    fun CallTest(){
        val commands = command.replace("\\","")
        val tokens = token.replace("\\","")
    }
    fun String.runCommand():String? {
        try {
            val parts = this.split("　".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            return proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
            return null
        }
    }
    fun accessCode(){
    }
}

