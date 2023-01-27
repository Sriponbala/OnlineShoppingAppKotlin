package org.bala.ui

import org.bala.enums.UserAccountField
import org.bala.enums.UserAccountMenu
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.data.User
import org.sri.interfaces.UserAccountActivitiesContract

internal class UserAccountPage(private val userAccountActivities: UserAccountActivitiesContract) {

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
            IOHandler.showMenu("YOUR ACCOUNT", userAccountMenu)
            when(IOHandler.getUserChoice(userAccountMenu)) {

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
            IOHandler.showMenu("EDIT USER DETAILS", userAccountFields)
            println("SELECT THE FIELD TO EDIT:")
            when(IOHandler.getUserChoice(userAccountFields)) {

                UserAccountField.Name -> {
                    val name = IOHandler.readName()
                    if(userAccountActivities.updateName(name)) {
                        println("Updated name successfully!")
                    } else println("Failed to update name!")
                }

                UserAccountField.Email -> {
                    val email = IOHandler.readEmail()
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