package org.sri.interfaces

import org.sri.data.Address
import org.sri.data.LineItem
import org.sri.data.Order
import org.sri.data.Product
import org.sri.enums.Payment
import java.time.LocalDate

interface OrdersHistoryActivitiesContract {

        fun getOrdersHistory(userId: String): MutableList<Triple<Order, Product, Int>>

        fun addLineItemsToDb(lineItems: MutableList<LineItem>)

        fun createOrder(userId: String, orderedDate: LocalDate, shippingAddress: Address, payment: Payment)

        fun createOrderAndLineItemMapping(lineItems: MutableList<LineItem>)

}