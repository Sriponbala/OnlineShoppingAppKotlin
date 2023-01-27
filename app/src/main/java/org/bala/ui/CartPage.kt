package org.bala.ui

import org.bala.enums.ProductQuantityManagement
import org.bala.enums.ProductsManagementMenu
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.data.CartItem
import org.sri.data.Product
import org.sri.enums.StockStatus
import org.sri.interfaces.CartActivitiesContract

internal class CartPage(private val cartActivities: CartActivitiesContract) {

    private var isCartEmpty = false
    private lateinit var cartItems: MutableList<Triple<CartItem, Product, StockStatus>>
    private lateinit var accountInfo: AccountInfo
    private lateinit var cartId: String

    fun initializer(accountInfo: AccountInfo) {
        this.accountInfo = accountInfo
        this.cartId = accountInfo.cartId
    }

    fun openCartPage(navigator: Navigator) {
        while(true) {
            isCartEmpty = cartActivities.checkIfCartIsEmpty()
            if(!isCartEmpty) {
                cartItems = cartActivities.getCartItems()
                displayCartItems()
                val cartActivitiesDashboard = ProductsManagementMenu.values()
                IOHandler.showMenu("CART MENU", cartActivitiesDashboard)
                when(IOHandler.getUserChoice(cartActivitiesDashboard)) {
                    ProductsManagementMenu.SELECT_A_PRODUCT -> {
                        val option = IOHandler.readOption("cart item", cartItems.size)
                        val cartItem = cartItems[option - 1]
                        doActivitiesOnSelectedItem(cartItem)
                    }
                    ProductsManagementMenu.PROCEED_TO_BUY -> {
                        val items = mutableListOf<Triple<CartItem, Product, StockStatus>>()
                        for(cartItem in cartItems) {
                            if(cartItem.third != StockStatus.OUTOFSTOCK) {
                                items.add(cartItem)
                            }
                        }
                        navigator.goToCheckOutPage(navigator, items, accountInfo)
                    }
                    ProductsManagementMenu.GO_BACK -> {
                        break
                    }
                }
            } else {
                println("No items found in cart!")
                break
            }
        }
    }

    private fun displayCartItems() {
        println("CART ITEMS: ")
        cartItems.forEachIndexed { index, cartItem ->
            println("""${index + 1}. Item Name        : ${cartItem.second.productName}
                |   Item price       : ${cartItem.second.price}
                |   Quantity         : ${cartItem.first.quantity}
                |   Status           : ${cartItem.third.status}
            """.trimMargin())
        }
        println("   Subtotal: ${cartActivities.calculateAndUpdateSubtotal(cartId, cartItems)}")
    }

    private fun doActivitiesOnSelectedItem(cartItem: Triple<CartItem, Product, StockStatus>) {
        val productQuantityManagement = ProductQuantityManagement.values()
        while(true) {
            IOHandler.showMenu("ACTIVITIES ON SELECTED PRODUCT", productQuantityManagement)
            when(IOHandler.getUserChoice(productQuantityManagement)) {
                ProductQuantityManagement.CHANGE_QUANTITY -> {
                    if(cartItem.third == StockStatus.INSTOCK) {
                        val quantity = IOHandler.getQuantity(cartItem.first.skuId, cartActivities)
                        if(IOHandler.confirm(1)) {
                            if(cartActivities.changeQuantityOfCartItem(cartId, cartItem.first.skuId, quantity)) {
                                println("Quantity changed!")
                            } else {
                                println("Failed to change quantity")
                            }
                        }
                    } else {
                        println("Product out of stock!")
                    }

                }
                ProductQuantityManagement.REMOVE -> {
                    if(cartActivities.removeFromCart(cartId, cartItem.first.skuId)){
                        println("Item removed from cart!")
                        break
                    } else {
                        println("Failed removing item from cart")
                        break
                    }
                }
                ProductQuantityManagement.GO_BACK -> {
                    break
                }
            }
        }
    }

}