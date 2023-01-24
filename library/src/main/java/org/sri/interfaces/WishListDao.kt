package org.sri.interfaces

import org.sri.data.Product
import org.sri.data.WishList

internal interface WishListDao {

    fun retrieveWishList(wishListId: String): WishList?

    fun retrieveWishListProducts(wishListId: String): ArrayList<Product>

    fun addAProductToWishList(wishListId: String, skuId: String)

    fun deleteProductFromWishList(wishListId: String, skuId: String)

    fun createAndGetWishListId(userId: String): String

}
