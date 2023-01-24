package org.bala.ui

import org.bala.enums.UserAccountField
import org.bala.enums.UserAccountMenu
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.data.User
import org.sri.interfaces.UserAccountActivitiesContract

class UserAccountPage(private val userAccountActivities: UserAccountActivitiesContract): DashboardServices {

    private lateinit var accountInfo: AccountInfo
    private lateinit var user: User

    fun initializer(accountInfo: AccountInfo) {
        this.accountInfo = accountInfo
    }

    private fun displayUserDetails(user: User) {
        println("---------------YOUR PROFILE--------------")
        println("""|NAME   : ${user.userName} 
                   |MOBILE : ${user.userMobile} 
                   |EMAIL  : ${user.userEmail}""".trimMargin())
    }

    fun openUserAccountPage(navigator: Navigator) {
        this.user = userAccountActivities.retrieveUser()
        displayUserDetails(this.user)
        val userAccountMenu = UserAccountMenu.values()
        while(true) {
            super.showDashboard("YOUR ACCOUNT", userAccountMenu)
            when(super.getUserChoice(userAccountMenu)) {

                UserAccountMenu.VIEW_WISHLIST -> {
                    navigator.goToWishListPage(navigator, accountInfo.wishListId)
                }

                UserAccountMenu.VIEW_ORDERS_HISTORY -> {
                    navigator.goToOrdersPage(accountInfo.userId)
                }

                UserAccountMenu.EDIT_ACCOUNT -> {
                    editUserAccountDetails(navigator)
                }

                UserAccountMenu.GO_BACK -> break
            }
        }
    }


    private fun editUserAccountDetails(navigator: Navigator) {
        val userAccountFields = UserAccountField.values()
        while(true) {
            super.showDashboard("EDIT USER DETAILS", userAccountFields)
            println("SELECT THE FIELD TO EDIT:")
            when(super.getUserChoice(userAccountFields)) {

                UserAccountField.Name -> {
                    var name: String
                    do{
                        println("ENTER NAME: ")
                        name = readLine()!!
                    } while(IOHandler.fieldValidation(name))
                    if(userAccountActivities.updateName(name)) {
                        println("Updated name successfully!")
                    } else println("Failed to update name!")
                }

                UserAccountField.Email -> {
                    var email: String
                    do {
                        println("ENTER EMAIL: ")
                        email = readLine()!!
                    } while(!IOHandler.fieldValidation(email) && !IOHandler.validateEmail(email))
                    if(userAccountActivities.updateEmail(email)) {
                        println("Updated email successfully!")
                    } else println("Failed to update email!")

                }
                UserAccountField.Addresses -> {
                    navigator.goToAddressPage()
                }

                UserAccountField.Back -> {
                    break
                }
            }
        }
    }

}