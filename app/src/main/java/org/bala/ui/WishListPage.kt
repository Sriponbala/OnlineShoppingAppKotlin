package org.bala.ui

import org.bala.enums.WishListMenu
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.Product
import org.sri.data.WishList
import org.sri.interfaces.WishListActivitiesContract

class WishListPage(private val wishListsActivities: WishListActivitiesContract): DashboardServices {

    private lateinit var wishListProductSkus : ArrayList<Product>
    private var isEmptyWishList: Boolean = true
    private lateinit var wishListId: String

    fun initializer(wishListId: String) {
        this.wishListId = wishListId
    }

    fun openWishListPage(navigator: Navigator) {
        while(true) {
            wishListProductSkus = wishListsActivities.getWishListProducts(wishListId)
            checkIfWishListIsEmpty(wishListProductSkus)
            displayWishListProducts(isEmptyWishList)
            if(!isEmptyWishList) {
                if(IOHandler.confirm()) {
                    val selectedProduct = selectAProduct()
                    val wishListMenu = WishListMenu.values()
                    while(true) {
                        super.showDashboard("WISHLIST DASHBOARD", wishListMenu)
                        when(super.getUserChoice(wishListMenu)) {

                            WishListMenu.VIEW_PRODUCT -> {
                                println(selectedProduct)
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

    private fun selectAProduct(): String {
        var option: Int
        var selectedProduct = ""
        while(true){
            println("SELECT A PRODUCT: ")
            try{
                val userInput = readLine()!!
                option = userInput.toInt()
                if(IOHandler.checkValidRecord(option,wishListProductSkus.size)) {
                    selectedProduct = wishListProductSkus[option - 1].skuId
                }
                break
            } catch(exception: Exception) {
                println("Enter valid option!")
            }
        }
        return selectedProduct
    }

    private fun displayWishListProducts(isEmptyWishList: Boolean) {
        println("-------------------${WishList.WISHLISTNAME}----------------------")
        if(isEmptyWishList) {
            println("        No items found        ")
        } else {
            wishListProductSkus.forEachIndexed { index, productSku ->
                println("${index + 1}. ${productSku.productName} - ${productSku.price}")
            }
        }
    }

    private fun checkIfWishListIsEmpty(wishListProductSkus: ArrayList<Product>?) {
        isEmptyWishList = wishListProductSkus?.isEmpty() == true
    }
}