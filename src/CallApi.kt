package jp.making.felix

import java.io.IOException
import java.util.concurrent.TimeUnit

class CallApi(){
    fun CallTest(command:String):String{
        val result = command.runCommand()
        result?.apply {
            return this
        }
        return "failed"
    }
    fun String.runCommand():String? {
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
    //private fun getCode(appId:)

}

