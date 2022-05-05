package com.example.fromdeskhelper.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

data class RegisterResult(
    val success: LoggedInUserView? = null,
    val error: String? = null
)