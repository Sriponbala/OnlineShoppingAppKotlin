package org.bala.ui

import org.bala.enums.WishListMenu
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.Product
import org.sri.data.WishList
import org.sri.interfaces.WishListActivitiesContract

internal class WishListPage(private val wishListsActivities: WishListActivitiesContract) {

    private lateinit var wishListProducts : ArrayList<Product>
    private var isEmptyWishList: Boolean = true
    private lateinit var wishListId: String

    fun initializer(wishListId: String) {
        this.wishListId = wishListId
    }

    fun openWishListPage(navigator: Navigator) {
        while(true) {
            wishListProducts = wishListsActivities.getWishListProducts(wishListId)
            checkIfWishListIsEmpty(wishListProducts)
            displayWishListProducts(isEmptyWishList)
            if(!isEmptyWishList) {
                if(IOHandler.confirm(0)) {
                    val option = IOHandler.readOption("product", wishListProducts.size)
                    val selectedProduct = wishListProducts[option - 1].skuId
                    val wishListMenu = WishListMenu.values()
                    while(true) {
                        IOHandler.showMenu("WISHLIST MENU", wishListMenu)
                        when(IOHandler.getUserChoice(wishListMenu)) {

                            WishListMenu.VIEW_PRODUCT -> {
                                navigator.goToShopPage(selectedProduct)
                                break
                            }

                            WishListMenu.DELETE_PRODUCT -> {
                                wishListsActivities.removeProductFromWishList(wishListId, selectedProduct)
                                break
                            }

                            WishListMenu.GO_BACK -> {
                                break
                            }
                        }
                    }
                } else {
                    break
                }
            } else {
                break
            }
        }
    }

    private fun displayWishListProducts(isEmptyWishList: Boolean) {
        println("-------------------${WishList.WISHLISTNAME}----------------------")
        if(isEmptyWishList) {
            println("        No items found        ")
        } else {
            wishListProducts.forEachIndexed { index, product ->
                println("${index + 1}. ${product.productName} - ${product.price}")
            }
        }
    }

    private fun checkIfWishListIsEmpty(wishListProducts: ArrayList<Product>?) {
        isEmptyWishList = wishListProducts?.isEmpty() == true
    }
}