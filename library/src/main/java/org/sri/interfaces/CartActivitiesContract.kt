package org.sri.interfaces

import org.sri.data.CartItem
import org.sri.data.Product
import org.sri.enums.StockStatus

interface CartActivitiesContract {

    fun createAndGetCartId(userId: String): String

    fun getCart(cartId: String): Boolean

    fun getCartItems(): MutableList<Triple<CartItem, Product, StockStatus>>

    fun addToCart(cartId: String, skuId: String): Boolean

    fun removeFromCart(cartId: String, skuId: String): Boolean

    fun removeFromCart(cartId: String, skuId: String, quantity: Int): Boolean

    fun checkIfCartIsEmpty(): Boolean

    fun calculateAndUpdateSubtotal(cartId: String, cartItems: MutableList<Triple<CartItem, Product, StockStatus>>): Float

    fun getAvailableQuantityOfProduct(skuId: String): Int

    fun changeQuantityOfCartItem(cartId: String, skuId: String, quantity: Int): Boolean

}