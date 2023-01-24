package org.sri.helper

import org.sri.interfaces.*
import org.sri.utils.*
import org.sri.activity.*

object InstanceProvider {

    val userAccountActivities: UserAccountActivitiesContract by lazy { UserAccountActivities(UtilityDaoImplementation.getInstance(), UserDaoImplementation.getInstance()) }

    val cartActivities: CartActivitiesContract by lazy { CartActivities(UtilityDaoImplementation.getInstance(), CartDaoImplementation.getInstance()) }

    val productActivities: ProductActivitiesContract by lazy { ProductActivities(UtilityDaoImplementation.getInstance(), ProductsDaoImplementation.getInstance()) }

    val ordersHistoryActivities: OrdersHistoryActivitiesContract by lazy { OrdersHistoryActivities(OrdersDaoImplementation.getInstance()) }

    val wishListsActivities: WishListActivitiesContract by lazy { WishListsActivities(UtilityDaoImplementation.getInstance(), WishListsDaoImplementation.getInstance()) }

    val checkOutActivities: CheckOutActivitiesContract by lazy { CheckOutActivities(cartActivities, productActivities, ordersHistoryActivities) }

}