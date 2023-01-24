package org.sri.interfaces

import org.sri.data.Cart

internal interface UtilityDao {

    fun checkUniqueUser(mobile: String): Boolean

    fun validateLoginCredentials(mobile: String, password: String) : Boolean

    fun checkIfUserExists(userId: String): Boolean

    fun checkIfUserAccountInfoExists(userId: String): Boolean

    fun checkIfWishListExists(wishListId: String): Boolean

    fun checkIfProductIsInUserWishList(wishListId: String, skuId: String): Boolean

    fun checkIfProductExists(skuId: String): Boolean

    fun checkIfCartExists(cartId: String): Boolean

    fun checkIfItemIsInCart(cart: Cart, skuId: String): Boolean

    fun checkIfAddressExists(addressId: String): Boolean

}