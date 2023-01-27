package org.sri.activity

import org.sri.data.Cart
import org.sri.data.CartItem
import org.sri.data.Product
import org.sri.enums.StockStatus
import org.sri.helper.InstanceProvider
import org.sri.interfaces.CartActivitiesContract
import org.sri.interfaces.CartDao
import org.sri.interfaces.ProductsDao
import org.sri.interfaces.UtilityDao
import org.sri.utils.ProductsDaoImplementation

internal class CartActivities(private val utility: UtilityDao, private val cartDao: CartDao): CartActivitiesContract {

    private var cart: Cart? = null
    private val productsDao: ProductsDao = ProductsDaoImplementation.getInstance()
    private val productActivities = InstanceProvider.productActivities

    override fun createAndGetCartId(userId: String): String {
        return cartDao.createAndGetCartId(userId)
    }

    override fun getCart(cartId: String): Boolean {
        return if(utility.checkIfCartExists(cartId)) {
            this.cart = cartDao.getCart(cartId)
            this.cart != null
        } else false
    }

    override fun getCartItems(): MutableList<Triple<CartItem, Product, StockStatus>> {
        val cartItems: MutableList<Triple<CartItem, Product, StockStatus>> = mutableListOf()
        this.cart?.cartItems?.forEach { cartItem ->
            val productDetails = productsDao.retrieveProductDetails(cartItem.skuId)
            productDetails?.let {
                val cartItemDetails = Triple(cartItem, productDetails.first, productDetails.second)
                cartItems.add(cartItemDetails)
            }
        }
        return cartItems
    }

    override fun addToCart(cartId: String, skuId: String): Boolean {
        val itemAddedToCart: Boolean = if(utility.checkIfCartExists(cartId)) {
            if(!utility.checkIfItemIsInCart(this.cart!!, skuId)) {
                cartDao.addToCart(cartId, skuId)
                true
            } else false
        } else false
        return itemAddedToCart
    }

    override fun removeFromCart(cartId: String, skuId: String): Boolean {
        val itemRemovedFromCart: Boolean = if(utility.checkIfCartExists(cartId)) {
            if(utility.checkIfItemIsInCart(this.cart!!, skuId)) {
                cartDao.removeFromCart(cartId, skuId)
                true
            } else false
        } else false
        return itemRemovedFromCart
    }

    override fun removeFromCart(cartId: String, skuId: String, quantity: Int): Boolean {
        val isItemRemoved: Boolean = if(utility.checkIfCartExists(cartId)) {
            if(utility.checkIfItemIsInCart(this.cart!!,skuId)) {
                if(quantity >= cartDao.getCartItemQuantity(cartId, skuId)) {
                    removeFromCart(cartId, skuId)
                    true
                } else {
                    cartDao.updateItemQuantity(cartId, skuId, quantity)
                    true
                }
            } else false
        } else false
        return isItemRemoved
    }

    override fun checkIfCartIsEmpty(): Boolean {
        return this.cart?.cartItems?.isEmpty() ?: true
    }

    override fun calculateAndUpdateSubtotal(cartId: String, cartItems: MutableList<Triple<CartItem, Product, StockStatus>>): Float {
        var subTotal = 0f
        if(utility.checkIfCartExists(cartId)) {
            for(cartItem in cartItems) {
                if(cartItem.third == StockStatus.INSTOCK) {
                    subTotal += (cartItem.second.price * cartItem.first.quantity)
                }
            }
            cartDao.updateSubtotal(cartId, subTotal)
        }
        return subTotal
    }

    override fun getAvailableQuantityOfProduct(skuId: String): Int {
        return productActivities.getAvailableQuantityOfProduct(skuId)
    }

    override fun changeQuantityOfCartItem(cartId: String, skuId: String, quantity: Int): Boolean {
        return if(utility.checkIfCartExists(cartId)) {
            if(utility.checkIfItemIsInCart(this.cart!!, skuId)) {
                cartDao.changeItemQuantity(cartId , skuId, quantity)
                calculateAndUpdateSubtotal(cartId, getCartItems())
                true
            } else false
        } else false
    }

}
