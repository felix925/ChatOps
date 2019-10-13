package jp.making.felix

import java.io.IOException
import java.util.concurrent.TimeUnit

data class Repository(val value: String = "daizu-ChatOps")

class CallApi(repository: Repository){
    private val TOKEN:String = System.getenv("TOKEN")
    private val command = "curl -u \"felix925\" https://api.github.com"
    fun CallTest():String{
        command.runCommand()?.apply {
            return this
        }
        return ""
    }
    fun String.runCommand(): String? {
        try {
            val parts = this.split("".toRegex())
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