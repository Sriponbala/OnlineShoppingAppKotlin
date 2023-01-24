package org.sri.helper

internal object Helper {
    private var userId = 1
    private var addressId = 1
    private var cartId = 1
    private var wishListId = 1
    private var orderId = 1
    private var productId = 1
    private var lineItemId = 1

    fun generateProductId(): String = "PDT${productId++}"

    fun generateUserId(): String = "USR${userId++}"

    fun generateAddressId(): String = "ARS${addressId++}"

    fun generateCartId(): String = "CRT${cartId++}"

    fun generateWishListId(): String = "WL${wishListId++}"

    fun generateOrderId(): String = "ODR${orderId++}"

    fun generateLineItemId(): String = "LID${lineItemId++}"
}
