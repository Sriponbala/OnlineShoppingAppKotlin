package org.bala.ui

import org.sri.interfaces.OrdersHistoryActivitiesContract
import java.time.format.DateTimeFormatter

internal class OrdersPage(private val ordersHistoryActivities: OrdersHistoryActivitiesContract) {

    fun displayOrdersHistory(userId: String) {
        val ordersHistory = ordersHistoryActivities.getOrdersHistory(userId)
        println("-----------------ORDERS HISTORY---------------------")
        if(ordersHistory.isEmpty()){
            println("         No orders found         ")
        } else {
            ordersHistory.forEachIndexed { index, orderDetails ->
                println("""${index + 1}. PRODUCT NAME   : ${orderDetails.second.productName}
                    |   PRODUCT PRICE    : ${orderDetails.second.price}
                    |   QUANTITY         : ${orderDetails.third}
                    |   TOTAL PRICE      : ${orderDetails.second.price * orderDetails.third}
                    |   ORDERED DATE     : ${orderDetails.first.orderedDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))}
                    |   SHIPPING ADDRESS : ${orderDetails.first.shippingAddress.doorNo}, ${orderDetails.first.shippingAddress.flatName}, ${orderDetails.first.shippingAddress.street}, ${orderDetails.first.shippingAddress.area}, ${orderDetails.first.shippingAddress.city}, ${orderDetails.first.shippingAddress.state}, ${orderDetails.first.shippingAddress.pincode}
                """.trimMargin())
            }
        }
    }

}