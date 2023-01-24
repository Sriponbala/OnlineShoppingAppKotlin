package org.bala.utils

import org.sri.data.AccountInfo
import org.sri.data.Address
import org.sri.data.CartItem
import org.sri.data.Product
import org.sri.enums.Payment
import org.sri.enums.StockStatus

class Navigator {

    private val entryPage by lazy { PagesInstanceProvider.getInstanceOfEntryPage() }
    private val addressPage by lazy { PagesInstanceProvider.getInstanceOfAddressPage() }
    private val cartPage by lazy { PagesInstanceProvider.getInstanceOfCartPage() }
    private val checkOutPage by lazy { PagesInstanceProvider.getInstanceOfCheckOutPage() }
    private val homePage by lazy { PagesInstanceProvider.getInstanceOfHomePage() }
    private val ordersPage by lazy { PagesInstanceProvider.getInstanceOfOrdersPage() }
    private val paymentPage by lazy { PagesInstanceProvider.getInstanceOfPaymentPage() }
    private val shopPage by lazy { PagesInstanceProvider.getInstanceOfShopPage() }
    private val signUpPage by lazy { PagesInstanceProvider.getInstanceOfSignUpPage() }
    private val signInPage by lazy { PagesInstanceProvider.getInstanceOfSignInPage() }
    private val userAccountPage by lazy { PagesInstanceProvider.getInstanceOfUserAccountPage() }
    private val wishListPage by lazy { PagesInstanceProvider.getInstanceOfWishListPage() }

    fun goToEntryPage(navigator: Navigator) {
        entryPage.openEntryPage(navigator)
    }

    fun goToSignUpPage(navigator: Navigator) {
        signUpPage.signUp(navigator)
    }

    fun goToSignInPage(navigator: Navigator) {
        signInPage.signIn(navigator)
    }

    fun goToHomePage(navigator: Navigator) {
        homePage.openHomePage(navigator)
    }

    fun goToHomePage(navigator: Navigator, accountInfo: AccountInfo) {
        homePage.initializer(accountInfo)
        homePage.openHomePage(navigator)
    }

    fun goToShopPage(navigator: Navigator, isLoggedIn: Boolean) {
        shopPage.initializer(navigator, isLoggedIn)
        shopPage.openShopPage()
    }

    fun goToShopPage(navigator: Navigator, accountInfo: AccountInfo) {
        shopPage.initializer(navigator, accountInfo)
        shopPage.openShopPage()
    }

    fun goToShopPage(selectedProduct: String) {
        shopPage.productActivities(selectedProduct)
    }

    fun goToCartPage(navigator: Navigator, accountInfo: AccountInfo) {
        cartPage.initializer(accountInfo)
        cartPage.openCartPage(navigator)
    }

    fun goToUserAccountPage(navigator: Navigator, accountInfo: AccountInfo) {
        userAccountPage.initializer(accountInfo)
        userAccountPage.openUserAccountPage(navigator)
    }

    fun goToCheckOutPage(navigator: Navigator, skuId: String, accountInfo: AccountInfo) {
        checkOutPage.initializer(navigator, skuId, accountInfo)
        checkOutPage.openCheckOutPage()
    }

    fun goToCheckOutPage(navigator: Navigator, items: MutableList<Triple<CartItem, Product, StockStatus>>, accountInfo: AccountInfo) {
        checkOutPage.initializer(navigator, items, accountInfo)
        checkOutPage.openCheckOutPage()
    }

    fun goToWishListPage(navigator: Navigator, wishListId: String) {
        wishListPage.initializer(wishListId)
        wishListPage.openWishListPage(navigator)
    }

    fun goToOrdersPage(userId: String) {
        ordersPage.displayOrdersHistory(userId)
    }

    fun goToAddressPage() {
        addressPage.openAddressPage()
    }

    fun goToAddressPage(selectAddress: Boolean) {
        addressPage.setSelectAddress(selectAddress)
    }

    fun goToAddressPageAndSelectShippingAddress(): Address? {
        return addressPage.selectAddressForDelivery()
    }

    fun goToAddressPageAndDeselectAddress() {
        addressPage.deselectShippingAddress()
    }

    fun goToPaymentPage(): Payment {
        return paymentPage.getPaymentMethod()
    }

    fun goToPaymentPage(totalBill: Float) {
        paymentPage.pay(totalBill)
    }

}