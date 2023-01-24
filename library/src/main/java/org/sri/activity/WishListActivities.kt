package org.sri.activity

import org.sri.data.Product
import org.sri.interfaces.UtilityDao
import org.sri.interfaces.WishListActivitiesContract
import org.sri.interfaces.WishListDao

internal class WishListsActivities(private val utility: UtilityDao, private val wishListsDao: WishListDao): WishListActivitiesContract {

    override fun createAndGetWishListId(userId: String): String {
        return wishListsDao.createAndGetWishListId(userId)
    }

    override fun getWishListProducts(wishListId: String): ArrayList<Product> {
        return if(utility.checkIfWishListExists(wishListId)) {
            wishListsDao.retrieveWishListProducts(wishListId)
        } else arrayListOf()
    }

    override fun addProductToWishList(wishListId: String, skuId: String): Boolean {
        return if(utility.checkIfWishListExists(wishListId)) {
            if(!utility.checkIfProductIsInUserWishList(wishListId, skuId)) {
                wishListsDao.addAProductToWishList(wishListId, skuId)
                true
            } else false
        } else false
    }

    override fun removeProductFromWishList(wishListId: String, skuId: String): Boolean {
        return if(utility.checkIfWishListExists(wishListId)) {
            if(utility.checkIfProductIsInUserWishList(wishListId, skuId)) {
                wishListsDao.deleteProductFromWishList(wishListId, skuId)
                true
            } else false
        } else false
    }

}
