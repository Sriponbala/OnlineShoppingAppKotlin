package org.bala.ui

import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.helper.InstanceProvider

internal class SignInPage {

    private var mobile: String = ""
    private var password: String = ""
    private lateinit var userId: String
    private var accountInfo: AccountInfo? = null

    fun signIn(navigator: Navigator) {
        println("--------------SIGNIN PAGE--------------")
        try {
            val userAccountActivities = InstanceProvider.userAccountActivities
            val cartActivities = InstanceProvider.cartActivities
            getUserInputs()
            while (true) {
                if (IOHandler.confirm()) {
                    if (userAccountActivities.verifyAccount(mobile, password)) {
                        userId = userAccountActivities.getUserId(mobile)
                        if(userAccountActivities.getUser(userId)) {
                            accountInfo = userAccountActivities.getAccountInfo(userId)
                            if(accountInfo != null) {
                                if(cartActivities.getCart(accountInfo!!.cartId)) {
                                    println("SignIn Successful!")
                                    navigator.goToHomePage(navigator, accountInfo!!)
                                } else {
                                    println("Error...cart not found")
                                }
                            } else {
                                println("Invalid userId!")
                            }
                        } else {
                            println("User not found!")
                        }
                    } else {
                        println("SignIn failed!")
                    }
                    break
                } else {
                    break
                }
            }
        } catch (exception: Exception) {
            println("Some technical error occurred!")
        }
    }

    private fun getUserInputs() {
        do {
            println("""ENTER MOBILE NUMBER:
                |[Should contain 10 digits] 
            """.trimMargin())
            mobile = readLine()!!
        } while(IOHandler.fieldValidation(mobile) || !IOHandler.validateMobileNumber(mobile))

        do{
            println("""ENTER PASSWORD:
                |[Password can contain any of the following : a-zA-Z0-9!#@${'$'}%^&*_+`~]
                |[It should contain 4 to 8 characters]
            """.trimMargin())
            password = readLine()!!
        } while(IOHandler.fieldValidation(password) || !IOHandler.validatePasswordPattern(password))
    }

}