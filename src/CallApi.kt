package jp.making.felix

import kotlinx.css.command
import java.io.IOException
import java.util.concurrent.TimeUnit

class CallApi(token:String){
    private var commands: String = "curl¥ -X ¥POST¥ -H ¥\"Authorization: token ${token}\"¥ -H ¥\"Accept: application/vnd.github.everest-preview+json\"¥ -d ¥'{\"event_type\": \"custom.preview\"}'¥ -i ¥https://api.github.com/repos/felix925/ChatOps/dispatches"
    //private val commands = "curl -H \"Authorization: token ${token}\" -H \"Accept: application/vnd.github.everest-preview+json\" https://api.github.com/users/felix925"
    fun Calls():List<String>{
//        val result:String? = commands.runCommand()
//        result?.apply {
//            return this
//        }
//        return "failed"
        val command = commands.replace("\\","")
        val parts = command.split("¥".toRegex())
        return parts
    }
    private fun String.runCommand():String? {
        try {
            val commands = this.replace("\\","")
            val parts = commands.split("¥".toRegex())
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
}