package jp.making.felix

data class Repository(val value: String = "daizu-ChatOps")
data class Command(val command:String)
data class Token(val token:String)
data class GitHubSession(val accessToken:Token)
data class AppId(val appId:String)
data class AppSec(val appSec:String)