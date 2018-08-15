package com.systec.umeet.contants



class CommonException : RuntimeException {

    var errorCode: Int? = null
        private set

    constructor(errorCode: Int?) {
        this.errorCode = errorCode
    }

    constructor(errorCode: Int?, message: String) : super(message) {

        this.errorCode = errorCode
    }

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}



    companion object {
        private val serialVersionUID = 3043424573949178235L
    }
}
