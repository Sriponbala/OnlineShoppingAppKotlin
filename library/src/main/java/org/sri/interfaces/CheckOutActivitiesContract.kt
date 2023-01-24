package org.sri.interfaces

import org.sri.data.Address
import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.enums.Payment
import org.sri.enums.StockStatus
import java.time.LocalDate

interface CheckOutActivitiesContract {

    fun clearLineItems()

    fun removeFromCart(cartId: String, lineItem: LineItem, quantity: Int): Boolean

    fun clearCartItems(cartId: String, cartItems: MutableList<Pair<Product, StockStatus>>)

    fun createItemToBuy(skuId: String, orderedDate: LocalDate, quantity: Int, update: Boolean): MutableList<LineItem>

    fun getProductDetails(items: MutableList<LineItem>): MutableList<Pair<Product, StockStatus>>

    fun getAvailableQuantityOfProduct(skuId: String): Int

    fun getTotalBill(finalizedItems: MutableList<Pair<Product, StockStatus>>): Float

    fun updateStatusOfProducts()

    fun getLineItemQuantity(skuId: String): Int

    fun updateQuantityOfLineItem(product: Product, orderedDate: LocalDate, quantity: Int)

    fun addAllLineItemsToDb()

    fun createOrder(userId: String, orderedDate: LocalDate, shippingAddress: Address, payment: Payment)

    fun createOrderAndLineItemMapping(finalizedListOfItems: MutableList<LineItem>)

    fun getLineItems(): MutableList<LineItem>

}