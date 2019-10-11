package jp.making.felix

data class Repository(val value: String = "daizu-ChatOps")

class CallApi(val repository: Repository){
    private val TOKEN= System.getenv("TOKEN")
    fun CallTest(){
        val pb = ProcessBuilder(
            "curl",
            "-X",
            "POST -H",
            "\"Authorization: token ${TOKEN}\" ",
            "-H \"Accept: application/vnd.github.everest-preview+json\"",
            "-d '{\"event_type\": \"custom.preview\"}'",
            "-i",
            "https://api.github.com/repos/SoyBeansLab/${repository}/dispatches"
        )
        pb.start()
    }

}