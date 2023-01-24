package org.sri.utils

import org.sri.data.LineItem
import org.sri.data.Order
import org.sri.data.OrderIdLineItemMapping
import org.sri.database.Database
import org.sri.interfaces.OrdersDao

internal class OrdersDaoImplementation private constructor(private val userName: String = "root",
                                     private val password: String = "tiger"): OrdersDao {

    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { OrdersDaoImplementation() }
        fun getInstance(): OrdersDaoImplementation {
            return INSTANCE
        }
    }

    override fun retrieveOrdersHistory(userId: String): MutableList<OrderIdLineItemMapping> {
        val ordersHistory: MutableList<OrderIdLineItemMapping> = mutableListOf()
        for(order in database.orders) {
            if(userId == order.userId) {
                for(orderLineItemMapping in database.orderLineItemMappings) {
                    if(order.orderId == orderLineItemMapping.orderId) {
                        ordersHistory.add(orderLineItemMapping)
                    }
                }
            }
        }
        return ordersHistory
    }

    override fun getLineItemQuantity(skuId: String): Int {
        var quantity = 0
        for(lineItem in database.lineItems) {
            if(skuId == lineItem.skuId) {
                quantity += 1
            }
        }
        return quantity
    }

    override fun addLineItemsToDb(lineItems: MutableList<LineItem>) {
        for(lineItem in lineItems) {
            database.lineItems.add(lineItem)
        }
    }

    override fun createOrder(order: Order) {
        database.orders.add(order)
    }

    override fun getLineItem(lineItemId: String): LineItem? {
        var item: LineItem? = null
        for(lineItem in database.lineItems) {
            if(lineItemId == lineItem.lineItemId) {
                item = lineItem
                break
            }
        }
        return item
    }

    override fun getOrder(orderId: String): Order? {
        var order: Order? = null
        for(localOrder in database.orders) {
            if(orderId == localOrder.orderId) {
                order = localOrder
                break
            }
        }
        return order
    }

    override fun createOrderAndLineItemMapping(orderId: String, lineItems: MutableList<LineItem>) {
        for(lineItem in lineItems) {
            database.orderLineItemMappings.add(OrderIdLineItemMapping(orderId, lineItem.lineItemId))
        }
    }

}