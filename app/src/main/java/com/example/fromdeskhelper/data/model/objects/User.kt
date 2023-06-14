package com.example.fromdeskhelper.data.model.objects

data class UserPreferences(
    val show_presentation:Boolean,
    val is_login:Boolean
)

data class UserLogin(
    val employed:Boolean?,
    val anonime:Boolean?,
    val root:Boolean?,
    val admin:Boolean?,
    val user_acces:Boolean?,
)


object User {
    const val ROOT=0
    const val ADMIN= 1
    const val EMPLOYED=2
    const val USER=3
    const val USER_ACCESS=4
    const val ANONIME=5
}

object Function {
    const val Actions = 1
    const val Tasks = 2
    const val Arrange = 3
    const val Logger= 4
    const val sync = 5
    const val Find= 6
    const val Issue= 7
    const val Customers= 8
    const val Receipts= 9
    const val History= 10
    const val Configuration = 11
    const val Analytics= 12
    const val Scan= 13

    const val Purchase= 14
    const val Products= 15
    const val Stores= 16
    const val Link = 17
    const val Filters= 18
    const val Search= 19
    const val Interactions = 20
    const val MyAccounts= 21
    const val Information = 22
    const val Catering=23
}

object ErrorUser {
    const val INICIAR_USER = "Iniciar cliente"
    const val INICIAR="Iniciar Session"
    const val REGISTRARSE="Registrarse"
    const val EXIT="Exit"
    const val ANONIMO= "Iniciar Anonimamente"
}