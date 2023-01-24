package org.sri.interfaces

import org.sri.data.Product

interface WishListActivitiesContract {

        fun createAndGetWishListId(userId: String): String

        fun getWishListProducts(wishListId: String): ArrayList<Product>

        fun addProductToWishList(wishListId: String, skuId: String): Boolean

        fun removeProductFromWishList(wishListId: String, skuId: String): Boolean

}