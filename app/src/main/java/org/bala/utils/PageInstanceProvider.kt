package org.bala.utils

import org.bala.ui.*
import org.sri.helper.InstanceProvider

object PagesInstanceProvider {

    fun getInstanceOfEntryPage() = EntryPage()

    fun getInstanceOfSignUpPage() = SignUpPage()

    fun getInstanceOfSignInPage() = SignInPage()

    fun getInstanceOfHomePage() = HomePage()

    fun getInstanceOfUserAccountPage() = UserAccountPage(InstanceProvider.userAccountActivities)

    fun getInstanceOfAddressPage() = AddressPage(InstanceProvider.userAccountActivities)

    fun getInstanceOfWishListPage() = WishListPage(InstanceProvider.wishListsActivities)

    fun getInstanceOfOrdersPage() = OrdersPage(InstanceProvider.ordersHistoryActivities)

    fun getInstanceOfShopPage() = ShopPage(InstanceProvider.productActivities)

    fun getInstanceOfCartPage() = CartPage(InstanceProvider.cartActivities)

    fun getInstanceOfCheckOutPage() = CheckOutPage(InstanceProvider.checkOutActivities)

    fun getInstanceOfPaymentPage() = PaymentPage()

}