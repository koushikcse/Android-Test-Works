package com.example.myapplication.routeServer

import fi.iki.elonen.router.RouterNanoHTTPD
import java.io.IOException

class Route : RouterNanoHTTPD {

    @Throws(IOException::class)
    constructor(port: Int) : super(port) {
        addMappings()
    }

    @Throws(IOException::class)
    constructor(url: String, port: Int) : super(url, port) {
        addMappings()
    }


    override fun addMappings() {
        // todo fill in the routes
        addRoute("/", IndexHandler::class.java)
        addRoute("/test", ResponseManager::class.java)
        addRoute(ApiUri.START, ResponseManager::class.java)
        addRoute(ApiUri.STOP, ResponseManager::class.java)
        addRoute(ApiUri.IS_ACTIVE, ResponseManager::class.java)

    }
}