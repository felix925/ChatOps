package jp.making.felix

data class Repository(val value: String = "daizu-ChatOps")

class CallApi(repository: Repository){
    private val TOKEN= System.getenv("TOKEN")
    private val command = "curl -X POST -H \"Authorization: token ${TOKEN}\" -H \"Accept: application/vnd.github.everest-preview+json\" -d '{\"event_type\": \"custom.preview\"}' -i  https://api.github.com/repos/SoyBeansLab/${repository}/dispatches"
    private val comlist = command.split(Regex(" "))
    fun CallTest(){
        val pb = ProcessBuilder(comlist)
        val some = pb.start()
    }

}