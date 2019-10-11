package jp.making.felix

import khttp.delete as httpDelete

class CallAPI(val organizer:String = "SoyBeansLab", val repository:String = "daizu-ChatOps") {

    fun CallTest(){
        val url = "https://api.github.com/repos/${organizer}/${repository}/dispatches?token=5eb6b6a7e957065f71f2108bcdf5210525742fd2&Accept=application/vnd.github.everest-preview+json&event_type=custom.preview"
        khttp.post(url)
    }
}