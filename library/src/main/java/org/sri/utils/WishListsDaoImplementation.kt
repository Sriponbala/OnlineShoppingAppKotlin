package org.sri.utils

import org.sri.data.Product
import org.sri.data.WishList
import org.sri.data.WishListItem
import org.sri.database.Database
import org.sri.interfaces.WishListDao

internal class WishListsDaoImplementation private constructor(private val userName: String = "root",
                                        private val password: String = "tiger"): WishListDao {

    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { WishListsDaoImplementation() }
        fun getInstance(): WishListsDaoImplementation {
            return INSTANCE
        }
    }

    override fun retrieveWishList(wishListId: String): WishList? {
        var resultantWishList: WishList? = null
        for(wishList in database.usersWishList) {
            if(wishListId == wishList.wishListId) {
                resultantWishList = wishList
                break
            }
        }
        return resultantWishList
    }

    override fun retrieveWishListProducts(wishListId: String): ArrayList<Product> {
        val productsList: ArrayList<Product> = arrayListOf()
        for(wishList in database.usersWishList) {
            if(wishListId == wishList.wishListId) {
                for(wishListItem in wishList.wishListItems) {
                    for(productSku in database.productSkus) {
                        if(wishListItem.skuId == productSku.skuId) {
                            productsList.add(productSku)
                            break
                        }
                    }
                }
            }
        }
        return productsList
    }

    override fun addAProductToWishList(wishListId: String, skuId: String) {
        val wishListItem = WishListItem(wishListId, skuId)
        for(wishList in database.usersWishList) {
            if(wishListId == wishList.wishListId) {
                wishList.wishListItems.add(wishListItem)
                break
            }
        }
    }

    override fun deleteProductFromWishList(wishListId: String, skuId: String) {
        for(wishList in database.usersWishList) {
            if(wishListId == wishList.wishListId) {
                val iter = wishList.wishListItems.iterator()
                for(it in iter) {
                    if (it.skuId == skuId) {
                        iter.remove()
                        break
                    }
                }
                break
            }
        }
    }

    override fun createAndGetWishListId(userId: String): String {
        val wishList = WishList(userId)
        val wishListId = wishList.wishListId
        database.usersWishList.add(wishList)
        return wishListId
    }

}