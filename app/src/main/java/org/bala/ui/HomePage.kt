package org.bala.ui

import org.bala.enums.HomePageMenu
import org.bala.helper.DashboardServices
import org.bala.utils.Navigator
import org.sri.data.AccountInfo

internal class HomePage: DashboardServices {

    private lateinit var accountInfo: AccountInfo
    private var isLoggedIn: Boolean = false

    fun initializer(accountInfo: AccountInfo) {
        this.accountInfo = accountInfo
        this.isLoggedIn = true
    }

    fun openHomePage(navigator: Navigator) {
        val homePageActions: Array<HomePageMenu> = if(isLoggedIn) {
            arrayOf(HomePageMenu.VIEW_PRODUCTS, HomePageMenu.VIEW_CART, HomePageMenu.YOUR_ACCOUNT, HomePageMenu.SIGN_OUT)
        } else {
            HomePageMenu.values().copyOfRange(0,4)
        }
        while(true) {
            super.showDashboard("HOME PAGE", homePageActions)
            when(super.getUserChoice(homePageActions)) {
                HomePageMenu.VIEW_PRODUCTS -> {
                    if(isLoggedIn) {
                        navigator.goToShopPage(navigator, accountInfo)
                    } else {
                        navigator.goToShopPage(navigator, false)
                    }
                }
                HomePageMenu.VIEW_CART -> {
                    if(isLoggedIn) {
                        navigator.goToCartPage(navigator, accountInfo)
                    } else {
                        println("Login to add items in cart!")
                    }
                }
                HomePageMenu.YOUR_ACCOUNT -> {
                    if(isLoggedIn) {
                        navigator.goToUserAccountPage(navigator, accountInfo)
                    } else {
                        println("Login to your account!")
                    }
                }
                HomePageMenu.SIGN_OUT -> {
                    isLoggedIn = false
                    println("Signed out...")
                    break
                }
                HomePageMenu.BACK -> break
            }
        }
    }

}