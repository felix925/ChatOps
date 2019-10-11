package jp.making.felix

import khttp.delete as httpDelete

class CallAPI(val organizer:String = "SoyBeansLab", val repository:String = "daizu-ChatOps") {

    fun CallTest(){
        val url = "https://api.github.com/repos/${organizer}/${repository}/dispatches"
        khttp.post(url)
    }
}