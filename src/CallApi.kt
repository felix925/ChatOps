package jp.making.felix

import java.io.IOException
import java.util.concurrent.TimeUnit

class CallApi(repository: Repository){
    fun CallTest(command:Command):String{
        val result = command.toString().runCommand()
        result?.apply {
            return this
        }
        return "fail"
    }
    fun String.runCommand():String? {
        try {
            val command = this.replace("\\","")
            val parts = command.split("　".toRegex())
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

