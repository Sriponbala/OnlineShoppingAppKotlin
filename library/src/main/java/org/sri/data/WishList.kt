package org.sri.data

import org.sri.helper.Helper

data class WishList(val userId: String) {

    val wishListId = Helper.generateWishListId()
    val wishListItems: MutableList<WishListItem> = mutableListOf()
    companion object {
        const val WISHLISTNAME = "MY WISHLIST"
    }
}