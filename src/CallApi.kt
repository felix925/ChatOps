package jp.making.felix

import com.sun.xml.internal.ws.api.ha.StickyFeature
import java.io.IOException
import java.util.concurrent.TimeUnit

data class Repository(val value: String = "daizu-ChatOps")

class CallApi(repository: Repository){
    //private val TOKEN:String = System.getenv("TOKEN")
    private val TOKEN:String = System.getenv("APITOKEN")
    private val APPID:String = System.getenv("CL_ID")
    private val APPSEC:String = System.getenv("CL_SEC")
    private val CODE:String = System.getenv("CODE")
    private val token:String = "curl -X POST -d \"code=$CODE\" -d \"client_id=$APPID\" -d \"client_secret=$APPSEC\" https://github.com/login/oauth/access_token"
    //private val command = "curl¥ REST ¥-H ¥\"Accept: application/vnd.github.everest-preview+json\" ¥-d ¥'{\"event_type\":\"custom.preview\"}' ¥-i ¥https://api.github.com/repos/SoyBeansLab/${repository.value}/dispatches?access_token=${TOKEN}"
    private val command = ""
    fun CallTest():String{
        val commands = command.replace("\\","")
        val tokens = token.replace("\\","")
        tokens.runCommand()?.apply {
            return this
        }
        return ""
//        commands.runCommand()?.apply{
//            return this
//        }
//        return ""
    }
    fun String.runCommand():String? {
        try {
            val parts = this.split(" ".toRegex())
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
    fun tester():String{
        val commands = command.replace("\\","")
        val truecommands = commands.replace("¥","")
        return truecommands
    }
    fun String.trimCode():String{
        val commands = this.split("=")
        return commands[1]
    }

}

