package org.sri.interfaces

import org.sri.data.LineItem
import org.sri.data.Order
import org.sri.data.OrderIdLineItemMapping

internal interface OrdersDao {

    fun retrieveOrdersHistory(userId: String): MutableList<OrderIdLineItemMapping>

    fun getLineItemQuantity(skuId: String): Int

    fun addLineItemsToDb(lineItems: MutableList<LineItem>)

    fun createOrder(order: Order)

    fun getLineItem(lineItemId: String): LineItem?

    fun getOrder(orderId: String): Order?

    fun createOrderAndLineItemMapping(orderId: String, lineItems: MutableList<LineItem>)

}