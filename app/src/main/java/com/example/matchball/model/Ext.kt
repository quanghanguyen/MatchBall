package com.example.matchball.model

fun String.validateUpperCasePassword() : Boolean
{
    return matches(".*[A-Z].*".toRegex())
}

fun String.validateLowerCasePassword() : Boolean
{
    return matches(".*[a-z].*".toRegex())
}

fun String.validateLengthPassword() : Boolean {
    return length < 8
}

fun String.validateSpecialPassword() : Boolean {
    return matches(".*[@#\$%^&+=].*".toRegex())
}