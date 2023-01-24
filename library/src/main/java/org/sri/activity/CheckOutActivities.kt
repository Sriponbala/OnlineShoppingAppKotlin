package org.sri.activity

import org.sri.data.Address
import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.data.ProductInfo
import org.sri.enums.Payment
import org.sri.enums.StockStatus
import org.sri.interfaces.CartActivitiesContract
import org.sri.interfaces.CheckOutActivitiesContract
import org.sri.interfaces.OrdersHistoryActivitiesContract
import org.sri.interfaces.ProductActivitiesContract
import java.time.LocalDate

internal class CheckOutActivities(
    private val cartActivities: CartActivitiesContract,
    private val productActivities: ProductActivitiesContract,
    private val ordersHistoryActivities: OrdersHistoryActivitiesContract
): CheckOutActivitiesContract {

    private var lineItems: MutableList<LineItem> = mutableListOf()

    override fun clearLineItems() {
        this.lineItems = mutableListOf()
    }

    override fun removeFromCart(cartId: String, lineItem: LineItem, quantity: Int): Boolean {
        return cartActivities.removeFromCart(cartId, lineItem.skuId, quantity)
    }

    override fun clearCartItems(cartId: String, cartItems: MutableList<Pair<Product, StockStatus>>) {
        for (cartItem in cartItems) {
            cartActivities.removeFromCart(cartId, cartItem.first.skuId, getLineItemQuantity(cartItem.first.skuId))
        }
    }

    override fun createItemToBuy(
        skuId: String,
        orderedDate: LocalDate,
        quantity: Int,
        update: Boolean
    ): MutableList<LineItem> {
        val productDetails: MutableList<ProductInfo> = if (update) {
            productActivities.getProducts(skuId, quantity, lineItems)
        } else {
            productActivities.getProducts(skuId, quantity)
        }
        val tempLineItems: MutableList<LineItem> = mutableListOf()
        for (product in productDetails) {
            val lineItem = LineItem(skuId, product.productId, orderedDate)
            tempLineItems.add(lineItem)
            lineItems.add(lineItem)
        }
        return tempLineItems
    }

    override fun getProductDetails(items: MutableList<LineItem>): MutableList<Pair<Product, StockStatus>> {
        val finalizedSet: MutableSet<Pair<Product, StockStatus>> = mutableSetOf()
        val finalizedItems: MutableList<Pair<Product, StockStatus>> = mutableListOf()
        for (item in items) {
            val productDetails = productActivities.getProductDetails(item.skuId)
            productDetails?.let {
                finalizedSet.add(it)
            }
        }
        for (setItem in finalizedSet) {
            finalizedItems.add(setItem)
        }
        return finalizedItems
    }

    override fun getAvailableQuantityOfProduct(skuId: String): Int {
        return productActivities.getAvailableQuantityOfProduct(skuId)
    }

    override fun getTotalBill(finalizedItems: MutableList<Pair<Product, StockStatus>>): Float {
        var totalBill = 0f
        for (item in finalizedItems) {
            totalBill += (item.first.price * getLineItemQuantity(item.first.skuId))
        }
        return totalBill
    }

    override fun updateStatusOfProducts() {
        for (lineItem in lineItems) {
            productActivities.updateStatusOfProduct(lineItem)
        }
    }

    override fun getLineItemQuantity(skuId: String): Int {
        var quantity = 0
        for (lineItem in lineItems) {
            if (skuId == lineItem.skuId) {
                quantity += 1
            }
        }
        return quantity
    }

    override fun updateQuantityOfLineItem(product: Product, orderedDate: LocalDate, quantity: Int) {
        val newQuantity: Int
        if (quantity > getLineItemQuantity(product.skuId)) {
            newQuantity = quantity - getLineItemQuantity(product.skuId)
            createItemToBuy(product.skuId, orderedDate, newQuantity, update = true)
        } else if (quantity < getLineItemQuantity(product.skuId)) {
            newQuantity = getLineItemQuantity(product.skuId) - quantity
            var count = 0
            val iter = lineItems.iterator()
            for (it in iter) {
                if (count < newQuantity) {
                    if (product.skuId == it.skuId) {
                        iter.remove()
                        count++
                    }
                } else break
            }
        }
    }

    override fun addAllLineItemsToDb() {
        ordersHistoryActivities.addLineItemsToDb(lineItems)
    }

    override fun createOrder(userId: String, orderedDate: LocalDate, shippingAddress: Address, payment: Payment) {
        ordersHistoryActivities.createOrder(userId, orderedDate, shippingAddress, payment)
    }

    override fun createOrderAndLineItemMapping(finalizedListOfItems: MutableList<LineItem>) {
        ordersHistoryActivities.createOrderAndLineItemMapping(finalizedListOfItems)
    }

    override fun getLineItems() = lineItems

}