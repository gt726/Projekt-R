package com.example.projektr.data

data class User(
    var name: String,
    var email: String
) {
    constructor() : this(name = "", email = "")
}
