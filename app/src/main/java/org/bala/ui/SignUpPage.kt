package org.bala.ui

import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.helper.InstanceProvider

class SignUpPage {

    private var name: String = ""
    private var mobile: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private lateinit var cartId: String
    private lateinit var wishListId: String
    private lateinit var userId: String
    private var accountInfo: AccountInfo? = null

    fun signUp(navigator: Navigator) {
        println("--------------SIGNUP PAGE--------------")
        try {
            val userAccountActivities = InstanceProvider.userAccountActivities
            val wishListsActivities = InstanceProvider.wishListsActivities
            val cartActivities = InstanceProvider.cartActivities
            getUserInputs()
            while(true) {
                if (IOHandler.confirm()) {
                    if(userAccountActivities.verifyAccount(mobile)) {
                        val otp = IOHandler.generateOTP()
                        println("OTP : $otp")
                        while(true) {
                            println("ENTER THE OTP: ")
                            val currentOtp = readLine()!!
                            if(IOHandler.verifyOtp(currentOtp, otp)) {
                                userId = userAccountActivities.createAndGetUserId(name, mobile, email, password)
                                if(userAccountActivities.getUser(userId)) {
                                    wishListId = wishListsActivities.createAndGetWishListId(userId)
                                    cartId = cartActivities.createAndGetCartId(userId)
                                    if(userAccountActivities.createAccountInfo(userId, cartId, wishListId)) {
                                        accountInfo = userAccountActivities.getAccountInfo(userId)
                                        if(accountInfo != null) {
                                            if(cartActivities.getCart(accountInfo!!.cartId)) {
                                                println("SignUp Successful!")
                                                navigator.goToHomePage(navigator, accountInfo!!)
                                            } else {
                                                println("Error...cart not found!")
                                            }
                                        } else {
                                            println("Account Info not found!")
                                        }
                                    } else {
                                        println("Invalid userId!")
                                    }
                                    break
                                } else {
                                    println("User not found!")
                                    break
                                }
                            } else {
                                println("Incorrect OTP! Try again!")
                            }
                        }
                    } else {
                        println("User already exists!")
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
        do{
            println("ENTER NAME: ")
            name = readLine()!!
        } while(IOHandler.fieldValidation(name))

        do{
            println("""ENTER MOBILE NUMBER:
                |[Should contain 10 digits] 
            """.trimMargin())
            mobile = readLine()!!
        }while(IOHandler.fieldValidation(mobile) || !IOHandler.validateMobileNumber(mobile))

        do {
            println("""ENTER EMAIL:
                |[Format: localpart@example.com] 
            """.trimMargin())
            email = readLine()!!
        } while(!IOHandler.fieldValidation(email) && !IOHandler.validateEmail(email))

        do{
            println("""ENTER PASSWORD:
                |[Password can contain any of the following : a-zA-Z0-9!#@${'$'}%^&*_+`~]
                |[It should contain 4 to 8 characters]""".trimMargin())
            password = readLine()!!
        } while(IOHandler.fieldValidation(password) || !IOHandler.validatePasswordPattern(password))

        do{
            println("ENTER CONFIRM PASSWORD: ")
            confirmPassword = readLine()!!
        } while(IOHandler.fieldValidation(confirmPassword) || !IOHandler.confirmPassword(confirmPassword,password))
    }

}