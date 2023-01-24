package org.sri.data

data class CartItem(val cartId: String, val skuId: String) {
    var quantity: Int = 1
}