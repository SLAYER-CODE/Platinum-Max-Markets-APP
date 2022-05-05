package com.example.fromdeskhelper.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val repetError:Int?=null,
    val isDataValid: Boolean = false
)