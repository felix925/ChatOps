package jp.making.felix

import com.sun.xml.internal.ws.api.ha.StickyFeature
import java.io.IOException
import java.util.concurrent.TimeUnit

data class Repository(val value: String = "daizu-ChatOps")

class CallApi(repository: Repository){
    private val TOKEN:String = System.getenv("TOKEN")
    private val command = "curl¥ POST ¥-H ¥\"Authorization: token ${TOKEN}\" ¥-H ¥\"Accept: application/vnd.github.everest-preview+json\" ¥-d ¥'{\"event_type\":\"custom.preview\"}' ¥-i ¥https://api.github.com/repos/SoyBeansLab/${repository.value}/dispatches"
    //private val command = "curl -i https://api.github.com/users/defunkt"
    fun CallTest():String{
        val commands = command.replace("\\","")
        commands.runCommand()?.apply {
            return this
        }
        return ""
    }
    fun String.runCommand():String? {
        try {
            val parts = this.split("¥".toRegex())
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
}

