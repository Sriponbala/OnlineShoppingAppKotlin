package org.sri.activity

import org.sri.data.*
import org.sri.enums.Payment
import org.sri.helper.InstanceProvider
import org.sri.interfaces.OrdersDao
import org.sri.interfaces.OrdersHistoryActivitiesContract
import java.time.LocalDate

internal class OrdersHistoryActivities(private val ordersDao: OrdersDao): OrdersHistoryActivitiesContract {

    private lateinit var orderId: String
    private val productActivities = InstanceProvider.productActivities

    override fun getOrdersHistory(userId: String): MutableList<Triple<Order, Product, Int>> {
        val ordersHistory: MutableList<Triple<Order, Product, Int>> = mutableListOf()
        val tempOrdersList: MutableList<Pair<Order, Product>> = mutableListOf()
        val tempQuantityMap: MutableMap<String, Int> = mutableMapOf()
        val ordersHistoryDetails = getOrdersHistoryDetails(userId)
        for(orderDetails in ordersHistoryDetails) {
            if(tempOrdersList.isEmpty()) {
                val order = ordersDao.getOrder(orderDetails.orderId)
                order?.let { it_order ->
                    val lineItem = ordersDao.getLineItem(orderDetails.lineItemId)
                    lineItem?.let {
                        val productDetails = productActivities.getProductDetails(lineItem.skuId)
                        productDetails?.let {
                            val productSku = it.first// Pair<ProductSku, Status>.first = ProductSku
                            val tempOrder = Pair(it_order, productSku)
                            tempOrdersList.add(tempOrder)
                            tempQuantityMap[productSku.skuId] = 1
                        }
                    }
                }
            } else {
                val order = ordersDao.getOrder(orderDetails.orderId)
                order?.let { it_order ->
                    val lineItem = ordersDao.getLineItem(orderDetails.lineItemId)
                    lineItem?.let { it_lineItem ->
                        var flag = false
                        for(tempOrder in tempOrdersList) {
                            if(it_lineItem.skuId == tempOrder.second.skuId) {
                                tempQuantityMap[it_lineItem.skuId] = tempQuantityMap[it_lineItem.skuId]!! + 1
                                flag = true
                                break
                            }
                        }
                        if(!flag) {
                            val productSku = productActivities.getProductDetails(it_lineItem.skuId)?.first// Pair<ProductSku, Status>.first = ProductSku
                            productSku?.let {
                                val tempOrder = Pair(it_order, it)
                                tempOrdersList.add(tempOrder)
                                tempQuantityMap[it.skuId] = 1
                            }
                        }
                    }
                }
            }
        }
        for(tempOrder in tempOrdersList) {
            ordersHistory.add(Triple(tempOrder.first, tempOrder.second, tempQuantityMap[tempOrder.second.skuId]!!))
        }
        return ordersHistory
    }

    private fun getOrdersHistoryDetails(userId: String): MutableList<OrderIdLineItemMapping> {
        return ordersDao.retrieveOrdersHistory(userId)
    }

    override fun addLineItemsToDb(lineItems: MutableList<LineItem>) {
        ordersDao.addLineItemsToDb(lineItems)
    }

    override fun createOrder(userId: String, orderedDate: LocalDate, shippingAddress: Address, payment: Payment) {
        val order = Order(userId, orderedDate, shippingAddress, payment)
        this.orderId = order.orderId
        ordersDao.createOrder(order)
    }

    override fun createOrderAndLineItemMapping(lineItems: MutableList<LineItem>) {
        ordersDao.createOrderAndLineItemMapping(orderId, lineItems)
    }

}

