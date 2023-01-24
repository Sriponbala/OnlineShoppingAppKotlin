package org.sri.data

import org.sri.helper.Helper

data class User(var userName: String, val userMobile: String, var userEmail: String) {
    val userId = Helper.generateUserId()
}