package org.bala.ui

import org.bala.enums.Confirmation
import org.bala.enums.FilterActionsMenu
import org.bala.enums.ProductActivitiesMenu
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.bala.utils.Navigator
import org.sri.data.AccountInfo
import org.sri.data.Filter
import org.sri.data.Product
import org.sri.enums.*
import org.sri.helper.InstanceProvider
import org.sri.interfaces.CartActivitiesContract
import org.sri.interfaces.ProductActivitiesContract
import org.sri.interfaces.WishListActivitiesContract

internal class ShopPage(private val productActivities: ProductActivitiesContract) : DashboardServices {

    private val wishListsActivities: WishListActivitiesContract by lazy { InstanceProvider.wishListsActivities }
    private val cartActivities: CartActivitiesContract by lazy { InstanceProvider.cartActivities }
    private lateinit var navigator: Navigator
    private lateinit var productsList: MutableList<Pair<Product, StockStatus>>
    private var productSku: Pair<Product, StockStatus>? = null
    private var isEmptyProductsList = false
    private lateinit var accountInfo: AccountInfo
    private var isLoggedIn = false
    private lateinit var filterOption: FilterBy
    private lateinit var filter: Filter
    private var isFilterApplied = false
    private var productName: String = ""
    private val filterOptions: MutableMap<FilterBy, Filter> = mutableMapOf()

    fun initializer(
        navigator: Navigator,
        accountInfo: AccountInfo
    ) {
        this.navigator = navigator
        this.accountInfo = accountInfo
        this.isLoggedIn = true
    }

    fun initializer(navigator: Navigator, isLoggedIn: Boolean) {
        this.navigator = navigator
        this.isLoggedIn = isLoggedIn
    }

    fun openShopPage() {
        label@while(true) {
            productsList = productActivities.getProductsList()
            isEmptyProductsList = productsList.isEmpty()
            if(isEmptyProductsList) {
                displayProducts(true)
                break
            } else {
                while(true) {
                    productsList = productActivities.getProductsList()
                    while(true) {
                        if(productsList.isEmpty()) {
                            displayProducts(true)
                        } else {
                            displayProducts(false)
                        }
                        val filterActionsMenu = FilterActionsMenu.values()
                        super.showDashboard("FILTER DASHBOARD", filterActionsMenu)
                        when(super.getUserChoice(filterActionsMenu)) {
                            FilterActionsMenu.SEARCH_PRODUCT -> {
                                getUserInputs()
                                productsList = productActivities.getProductsList(productName)
                            }
                            FilterActionsMenu.APPLY_FILTER -> {
                                val categories = ProductCategory.values()
                                lateinit var category: ProductCategory
                                while(true) {
                                    if(IOHandler.confirm()) {
                                        println("Select a category:")
                                        super.showDashboard("CATEGORIES", categories)
                                        category = super.getUserChoice(categories)
                                        productsList = productActivities.getProductsList(category)
                                        println("Do you want to apply one more filter for ${category.category}s?")
                                        if(IOHandler.confirm()) {
                                            applyFilter(category)
                                            isFilterApplied = true
                                            break
                                        } else {
                                            println("Filtered by the category ${category.category}!")
                                            isFilterApplied = true
                                            break
                                        }
                                    } else {
                                        isFilterApplied = false
                                        break
                                    }
                                }
                            }
                            FilterActionsMenu.CLEAR_FILTER -> {
                                if(isFilterApplied) {
                                    productsList = productActivities.clearFilters(productName)
                                    filterOptions.clear()
                                    isFilterApplied = false
                                } else {
                                    println("Clear filter option disabled since no filter is applied!")
                                }
                            }
                            FilterActionsMenu.SELECT_A_PRODUCT -> {
                                if(productsList.isEmpty()) {
                                    println("No products found, so select option disabled!")
                                } else {
                                    val skuId = selectAProduct()
                                    if(IOHandler.confirm()) {
                                        productActivities(skuId)
                                    }
                                }
                            }
                            FilterActionsMenu.BACK -> {
                                break@label
                            }
                        }
                    }
                }
            }
        }
    }

    private fun displayProducts(isEmptyProductsList: Boolean) {
        println("--------------------PRODUCTS----------------------")
        if(isEmptyProductsList) {
            println("       No products found        ")
        } else {
            var sno = 1
            for(productDetails in productsList) {
                println("${sno++}. ${productDetails.first.productName} - Rs.${productDetails.first.price} - ${productDetails.first.category.category} - ${productDetails.second.status}")
            }
        }
    }

    fun productActivities(skuId: String) {
        var flag = false
        try {
            while (true) {
                productSku = productActivities.getProductDetails(skuId)
                productSku?.let { productSku ->
                    displayProductDetails(productSku)
                    val productActivitiesMenu = ProductActivitiesMenu.values()
                    super.showDashboard("PRODUCT DASHBOARD", productActivitiesMenu)
                    when (super.getUserChoice(productActivitiesMenu)) {
                        ProductActivitiesMenu.ADD_TO_CART -> {
                            if(isLoggedIn) {
                                if(productSku.second == StockStatus.INSTOCK) {
                                    if(cartActivities.addToCart(accountInfo.cartId, skuId)) {
                                        println("Product added to cart!")
                                    } else {
                                        println("Can't add to cart!")
                                    }
                                } else {
                                    println("Product Out of Stock! Can't add to cart!")
                                }
                            } else {
                                println("Add to cart button disabled! Login to your account!")
                            }
                        }

                        ProductActivitiesMenu.ADD_TO_WISHLIST -> {
                            if(isLoggedIn) {
                                if(wishListsActivities.addProductToWishList(accountInfo.wishListId, skuId)) {
                                    println("Product added to wishlist!")
                                } else {
                                    println("Product already added to wishlist!")
                                }
                            } else {
                                println("Wishlist button disabled! Login to your account!")
                            }
                        }

                        ProductActivitiesMenu.REMOVE_FROM_WISHLIST -> {
                            if(isLoggedIn) {
                                if(wishListsActivities.removeProductFromWishList(accountInfo.wishListId, skuId)) {
                                    println("Product removed from wishlist!")
                                } else {
                                    println("Product not yet added to wishlist!")
                                }
                            } else {
                                println("Wishlist button disabled! Login to your account!")
                            }
                        }

                        ProductActivitiesMenu.BUY_NOW -> {
                            if(isLoggedIn) {
                                if(productSku.second == StockStatus.INSTOCK) {
                                    navigator.goToCheckOutPage(navigator, skuId, accountInfo)
                                } else {
                                    println("Product out of stock!")
                                }
                            } else {
                                println("Buy now button disabled! Login to your account!")
                            }
                        }

                        ProductActivitiesMenu.GO_BACK -> {
                            flag = true
                        }
                    }
                }
                if(flag) {
                    break
                }
            }
        } catch(exception: Exception) {
            println("exception: $exception")
            println("Something went wrong!")
        }
    }
    private fun selectAProduct(): String { // returns skuId
        var option: Int
        var selectedProductId: String
        while(true){
            println("SELECT A PRODUCT: ")
            try{
                val userInput = readLine()!!
                option = userInput.toInt()
                if(IOHandler.checkValidRecord(option, productsList.size)) {
                    selectedProductId = productsList[option - 1].first.skuId
                    break
                } else {
                    println("Invalid option! Try again!")
                }
            } catch(exception: Exception) {
                println("Enter valid option!")
            }
        }
        return selectedProductId
    }

    private fun displayProductDetails(productSku: Pair<Product, StockStatus>) {
        println("---------------------------------------------")
        println("""PRODUCT NAME       : ${productSku.first.productName}
                  |PRODUCT PRICE      : Rs.${productSku.first.price}
        """.trimMargin())
        when(productSku.first) {
            is Product.Book -> {
                val selectedProductSku = productSku.first as Product.Book
                println("""CATEGORY           : ${selectedProductSku.category.category}
                          |BOOK TYPE          : ${selectedProductSku.bookType.type}
                          |PRODUCT STATUS     : ${productSku.second.status} 
                """.trimMargin())
            }
            is Product.Mobile -> {
                val selectedProductSku = productSku.first as Product.Mobile
                println("""CATEGORY           : ${selectedProductSku.category.category}
                          |BRAND              : ${selectedProductSku.brand.brandName}
                          |PRODUCT STATUS     : ${productSku.second.status}
                """.trimMargin())
            }
            is Product.Clothing -> {
                val selectedProductSku = productSku.first as Product.Clothing
                println("""CATEGORY           : ${selectedProductSku.category.category}
                          |COLOUR             : ${selectedProductSku.colour.colour}
                          |GENDER             : ${selectedProductSku.gender.gender}
                          |PRODUCT STATUS     : ${productSku.second.status}
                """.trimMargin())
            }
            is Product.Earphone -> {
                val selectedProductSku = productSku.first as Product.Earphone
                println("""CATEGORY           : ${selectedProductSku.category.category}
                          |BRAND              : ${selectedProductSku.brand.brandName}
                          |PRODUCT STATUS     : ${productSku.second.status}
                """.trimMargin())
            }
        }
    }

    private fun applyFilter(category: ProductCategory) {
        while(true) {
            lateinit var filterArray: ArrayList<FilterBy>
            filterOption = when(category) {
                ProductCategory.BOOK -> {
                    filterArray = arrayListOf(FilterBy.PRICE, FilterBy.STATUS, FilterBy.BOOKTYPE)
                    getFilterChoice("FILTERS FOR BOOK", filterArray.toTypedArray())
                }
                ProductCategory.MOBILE -> {
                    filterArray = arrayListOf(FilterBy.PRICE, FilterBy.STATUS, FilterBy.BRAND)
                    getFilterChoice("FILTERS FOR MOBILE", filterArray.toTypedArray())
                }
                ProductCategory.CLOTHING -> {
                    filterArray = arrayListOf(FilterBy.PRICE, FilterBy.STATUS, FilterBy.GENDER, FilterBy.COLOUR)
                    getFilterChoice("FILTERS FOR CLOTHING", filterArray.toTypedArray())
                }
                ProductCategory.EARPHONE -> {
                    filterArray = arrayListOf(FilterBy.PRICE, FilterBy.STATUS, FilterBy.BRAND)
                    getFilterChoice("FILTERS FOR EARPHONE", filterArray.toTypedArray())
                }
            }
            filter = when(filterOption) {
                FilterBy.PRICE -> {
                    Filter.PriceFilter(getFilterChoice("PRICE", Price.values()))
                }

                FilterBy.STATUS -> {
                    Filter.StatusFilter(getFilterChoice("STATUS", StockStatus.values()))
                }

                FilterBy.BRAND -> {
                    Filter.BrandFilter(getFilterChoice("BRAND", Brand.values()))
                }

                FilterBy.BOOKTYPE -> {
                    Filter.BookTypeFilter(getFilterChoice("BOOK TYPE", BookType.values()))
                }

                FilterBy.GENDER -> {
                    Filter.GenderFilter(getFilterChoice("GENDER", Gender.values()))
                }

                FilterBy.COLOUR -> {
                    Filter.ColourFilter(getFilterChoice("COLOUR", Colour.values()))
                }
            }
            filterOptions[filterOption] = filter
            if(confirm()) {
                println("Select further filters: ")
            } else break
        }
        productsList = productActivities.getProductsList(productName, category, filterOptions)
        for(i in productsList) {
            println("1. ${i.first}")
        }
    }

    private fun confirm(): Boolean {
        while(true) {
            println("Confirm: ")
            for (option in Confirmation.values()) {
                println("${option.ordinal + 1}. ${option.list[2]}")
            }
            try {
                val choice = readLine()!!
                val confirmationChoice = choice.toInt() // chance of exception
                if(IOHandler.checkValidRecord(confirmationChoice, Confirmation.values().size)) {
                    return when(Confirmation.values()[confirmationChoice - 1]) {
                        Confirmation.CONTINUE -> true
                        Confirmation.GO_BACK -> false
                    }
                } else {
                    println("Enter proper input: ")
                }
            } catch(exception: Exception) {
                println("Enter valid option!")
            }
        }
    }

    private fun <E: Enum<E>> getFilterChoice(title: String, filters: Array<E>): E {
        super.showDashboard(title, filters)
        return super.getUserChoice(filters)
    }

    private fun getUserInputs() {
        do {
            println("""ENTER PRODUCT NAME:
            """.trimMargin())
            productName = readLine()!!
        } while(IOHandler.fieldValidation(productName))
    }

}