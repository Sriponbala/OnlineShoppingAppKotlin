package org.bala.ui

import org.bala.enums.ProductQuantityManagement
import org.bala.enums.ProductsManagementMenu
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.data.CartItem
import org.sri.data.Product
import org.sri.enums.StockStatus
import org.sri.interfaces.CartActivitiesContract

class CartPage(private val cartActivities: CartActivitiesContract): DashboardServices {

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
                for(i in cartItems) {
                    println("CartItems OpenCart(): $i q: ${i.first.quantity} ")
                }
                displayCartItems()
                val cartActivitiesDashboard = ProductsManagementMenu.values()
                super.showDashboard("CART DASHBOARD", cartActivitiesDashboard)
                when(super.getUserChoice(cartActivitiesDashboard)) {
                    ProductsManagementMenu.SELECT_A_PRODUCT -> {
                        val cartItem = selectACartItem()
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
        for(i in cartItems) {
            println("CartItems in cartPage: $i q: ${i.first.quantity} ")
        }
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
            super.showDashboard("ACTIVITIES ON SELECTED PRODUCT", productQuantityManagement)
            when(super.getUserChoice(productQuantityManagement)) {
                ProductQuantityManagement.CHANGE_QUANTITY -> {
                    if(cartItem.third == StockStatus.INSTOCK) {
                        val quantity = getQuantity(cartItem.first.skuId)
                        if(IOHandler.confirm()) {
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

    private fun selectACartItem(): Triple<CartItem, Product, StockStatus> {
        var option: Int
        var selectedItem: Triple<CartItem, Product, StockStatus>
        while(true){
            println("SELECT AN ITEM: ")
            try{
                val userInput = readLine()!!
                option = userInput.toInt()
                if(IOHandler.checkValidRecord(option, cartItems.size)) {
                    selectedItem = cartItems[option - 1]
                    break
                } else {
                    println("Invalid option! Try again")
                }
            } catch(exception: Exception) {
                println("Enter valid option!")
            }
        }
        return selectedItem
    }

    private fun getQuantity(skuId: String): Int {
        var quantity = 1
        while(true) {
            if(IOHandler.confirm()) {
                println("ENTER THE QUANTITY REQUIRED: ")
                try {
                    val input = readLine()!!.toInt()
                    if(input in 1..4) {
                        val availableQuantity = cartActivities.getAvailableQuantityOfProduct(skuId)
                        if(availableQuantity >= input) {
                            quantity = input
                            break
                        } else {
                            println("Only $availableQuantity items available!")
                        }
                    } else {
                        println("You can select a maximum of 4 items!")
                    }
                } catch(exception: Exception) {
                    println("Enter valid value!")
                }
            } else break
        }
        return quantity
    }
}