package org.sri.data

import org.sri.helper.Helper

data class Cart(val userId: String) {
    val cartId = Helper.generateCartId()
    val cartItems: MutableList<CartItem> = mutableListOf()
    var subTotal: Float = 0f
}
