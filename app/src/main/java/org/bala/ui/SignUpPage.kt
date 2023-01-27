package org.bala.ui

import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.helper.InstanceProvider

internal class SignUpPage {

    private val userAccountActivities = InstanceProvider.userAccountActivities
    private val wishListsActivities = InstanceProvider.wishListsActivities
    private val cartActivities = InstanceProvider.cartActivities
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
            getUserInputs()
            while(true) {
                if (IOHandler.confirm(1)) {
                    val otp = IOHandler.generateOTP()
                    println("OTP : $otp")
                    while(true) {
                        val currentOtp = IOHandler.readOTP()
                        if(IOHandler.verifyOtp(currentOtp, otp)) {
                            userId = userAccountActivities.createAndGetUserId(name, mobile, email, password)
                            if(userAccountActivities.getUser(userId)) {
                                wishListId = wishListsActivities.createAndGetWishListId(userId)
                                cartId = cartActivities.createAndGetCartId(userId)
                                if(userAccountActivities.createAccountInfo(userId, cartId, wishListId)) {
                                    accountInfo = userAccountActivities.getAccountInfo(userId)
                                    if(accountInfo != null) {
                                        if(cartActivities.getCart(accountInfo!!.cartId)) {
                                            println("Sign up Successful!")
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
        name = IOHandler.readName()
        while(true) {
            mobile = IOHandler.readMobileNumber()
            if(userAccountActivities.verifyAccount(mobile)) {
                email = IOHandler.readEmail()
                password = IOHandler.readPassword()
                confirmPassword = IOHandler.readConfirmPassword(password)
                break
            } else {
                println("User already exists!")
            }
        }
    }

}