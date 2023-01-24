package org.sri.interfaces

import org.sri.data.Cart

internal interface CartDao {

    fun createAndGetCartId(userId: String): String

    fun getCart(cartId: String): Cart?

    fun addToCart(cartId: String, skuId: String)

    fun removeFromCart(cartId: String, skuId: String)

    fun clearCart(cartId: String)

    fun changeItemQuantity(cartId: String, skuId: String, quantity: Int)

    fun updateSubtotal(cartId: String, subTotal: Float)

    fun getCartItemQuantity(cartId: String, skuId: String): Int

    fun updateItemQuantity(cartId: String, skuId: String, quantity: Int)

}