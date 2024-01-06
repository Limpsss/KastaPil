package com.example.kastapil.models

data class DatabaseUserStructure(
    var username: String = "",
    var dartsThrown: Int = 0,
    var hitMapCounter: MutableMap<String, Int> = mutableMapOf(),
    var singleOutCounter: MutableMap<String, Int> = mutableMapOf(),
    var doubleOutCounter: MutableMap<String, Int> = mutableMapOf()
)

