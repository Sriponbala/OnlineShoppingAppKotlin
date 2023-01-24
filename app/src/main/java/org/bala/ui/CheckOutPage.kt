package org.bala.ui

import org.bala.enums.ProductQuantityManagement
import org.bala.enums.ProductsManagementMenu
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.*
import org.sri.enums.Payment
import org.sri.enums.StockStatus
import org.sri.interfaces.CheckOutActivitiesContract
import java.time.LocalDate

internal class CheckOutPage(private val checkOutActivities: CheckOutActivitiesContract): DashboardServices {

    private var finalizedListOfItems = mutableListOf<LineItem>()
    private lateinit var finalizedItems: MutableList<Pair<Product, StockStatus>>
    private var shippingAddress: Address? = null
    private var quantity = 1
    private lateinit var orderedDate: LocalDate
    private lateinit var payment: Payment
    private lateinit var accountInfo: AccountInfo
    private lateinit var item: LineItem
    private lateinit var items: MutableList<Triple<CartItem, Product, StockStatus>>
    private var totalBill: Float = 0f
    private var isNavigatedFromCartPage: Boolean = false
    private lateinit var navigator: Navigator

    fun initializer(navigator: Navigator, skuId: String, accountInfo: AccountInfo) {
        this.navigator = navigator
        this.accountInfo = accountInfo
        this.orderedDate = IOHandler.generateOrderedDate()
        finalizedListOfItems = checkOutActivities.createItemToBuy(skuId, orderedDate, 1, update = false)
    }

    fun initializer(navigator: Navigator, items: MutableList<Triple<CartItem, Product, StockStatus>>, accountInfo: AccountInfo) {
        this.navigator = navigator
        this.accountInfo = accountInfo
        this.items = items
        this.orderedDate = IOHandler.generateOrderedDate()
        for(product in items) {
            val selectedItems = checkOutActivities.createItemToBuy(product.first.skuId, orderedDate, product.first.quantity, update = false)
            for(item in selectedItems) {
                finalizedListOfItems.add(item)
            }
        }
        this.isNavigatedFromCartPage = true
    }

    fun openCheckOutPage() {
        while(true) {
            this.finalizedItems = checkOutActivities.getProductDetails(finalizedListOfItems)
            displayItemDetails(finalizedItems)
            val checkOutPageDashboard = ProductsManagementMenu.values()
            super.showDashboard("CHECK OUT PAGE", checkOutPageDashboard)
            when(super.getUserChoice(checkOutPageDashboard)) {
                ProductsManagementMenu.SELECT_A_PRODUCT -> {
                    if(finalizedItems.isNotEmpty()) {
                        val (product, _) = selectAnItem()
                        doActivitiesOnSelectedItem(product)
                    } else {
                        println("No items selected to buy!")
                    }
                }
                ProductsManagementMenu.PROCEED_TO_BUY -> {
                    if(finalizedItems.isNotEmpty()) {
                        proceedToBuy()
                    } else {
                        println("No items selected to buy!")
                    }
                }
                ProductsManagementMenu.GO_BACK -> {
                    finalizedListOfItems.clear()
                    checkOutActivities.clearLineItems()
                    finalizedItems.clear()
                    break
                }
            }
        }
    }

    private fun proceedToBuy() {
        try {
            label@while(true) {
                println("SELECT AN ADDRESS: ")
                navigator.goToAddressPage(true)
                shippingAddress = navigator.goToAddressPageAndSelectShippingAddress()
                if(shippingAddress != null) {
                    if(IOHandler.confirm()) {
                        while(true) {
                            println("SELECT MODE OF PAYMENT: ")
                            payment = navigator.goToPaymentPage()
                            println("DO YOU WANT TO PLACE ORDER?")
                            if(IOHandler.confirm()) {
                                totalBill = checkOutActivities.getTotalBill(finalizedItems)
                                checkOutActivities.addAllLineItemsToDb()
                                checkOutActivities.updateStatusOfProducts()
                                checkOutActivities.createOrder(accountInfo.userId, orderedDate, shippingAddress!!, payment)
                                checkOutActivities.createOrderAndLineItemMapping(finalizedListOfItems)
                                navigator.goToPaymentPage(totalBill)
                                if(!isNavigatedFromCartPage) {
                                    item = finalizedListOfItems[0]
                                    if (checkOutActivities.removeFromCart(accountInfo.cartId, item, quantity)) {
                                        println("Order placed! Item removed from cart")
                                    } else {
                                        println("Order placed!")
                                    }
                                } else {
                                    checkOutActivities.clearCartItems(accountInfo.cartId, finalizedItems)
                                }
                                this.finalizedListOfItems.clear()
                                this.finalizedItems.clear()
                                checkOutActivities.clearLineItems()
                                navigator.goToAddressPageAndDeselectAddress()
                                break@label
                            } else {
                                break
                            }
                        }
                    } else {
                        navigator.goToAddressPageAndDeselectAddress()
                        break
                    }
                } else {
                    break
                }
            }
        } catch(exception: Exception) {
            println("Failed!")
        }
    }

    private fun displayItemDetails(
        finalizedItems: MutableList<Pair<Product, StockStatus>>
    ) {
        finalizedItems.forEachIndexed { index, item ->
            println("""${index + 1}. Item Name        : ${item.first.productName}
                |   Item price       : ${item.first.price}
                |   Quantity         : ${checkOutActivities.getLineItemQuantity(item.first.skuId)}
                |   Status           : ${item.second.status}
            """.trimMargin())
        }
    }

    private fun selectAnItem(): Pair<Product, StockStatus> {
        var option: Int
        var selectedItem: Pair<Product, StockStatus>
        while(true){
            println("SELECT AN ITEM: ")
            try{
                val userInput = readLine()!!
                option = userInput.toInt()
                if(IOHandler.checkValidRecord(option, finalizedItems.size)) {
                    selectedItem = finalizedItems[option - 1]
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
                        val availableQuantity = checkOutActivities.getAvailableQuantityOfProduct(skuId)
                        if(availableQuantity >= input) {
                            quantity = input
                            break
                        } else {
                            println("Only $availableQuantity items available!")
                        }
                    } else {
                        if(input < 1) {
                            println("You should select atleast 1 item!")
                        } else {
                            println("You can select a maximum of 4 items!")
                        }
                    }
                } catch(exception: Exception) {
                    println("Enter valid option!")
                }
            } else break
        }
        return quantity
    }

    private fun doActivitiesOnSelectedItem(product: Product) {
        val productQuantityManagement = ProductQuantityManagement.values()
        while(true) {
            super.showDashboard("ACTIVITIES ON SELECTED PRODUCT", productQuantityManagement)
            when(super.getUserChoice(productQuantityManagement)) {

                ProductQuantityManagement.CHANGE_QUANTITY -> {
                    println("selectedItm: $product")
                    println("finalItems: $finalizedItems  finalList: $finalizedListOfItems")
                    this.quantity = getQuantity(product.skuId)
                    if(IOHandler.confirm()) {
                        checkOutActivities.updateQuantityOfLineItem(product, orderedDate, quantity)
                        finalizedListOfItems = checkOutActivities.getLineItems()
                    }
                }

                ProductQuantityManagement.REMOVE -> {
                    println("selectedItm: $product")
                    println("finalItems: $finalizedItems  finalList: $finalizedListOfItems")
                    val iter = finalizedListOfItems.iterator()
                    for(it in iter) {
                        if (product.skuId == it.skuId) {
                            println("iter: $iter")
                            iter.remove()
                        }
                    }
                    val i = finalizedItems.iterator()
                    for(finalizedItem in i) {
                        if(product.skuId == finalizedItem.first.skuId) {
                            i.remove()
                        }
                    }
                    println("Item removed from finalised items!")
                    break
                }

                ProductQuantityManagement.GO_BACK -> {
                    break
                }
            }
        }
    }

}