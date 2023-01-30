package org.sri.utils

import org.sri.data.Cart
import org.sri.data.CartItem
import org.sri.database.Database
import org.sri.interfaces.CartDao

internal class CartDaoImplementation private constructor(private val userName: String = "root",
                                   private val password: String = "tiger"): CartDao {

    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { CartDaoImplementation() }
        fun getInstance(): CartDaoImplementation {
            return INSTANCE
        }
    }

    override fun createAndGetCartId(userId: String): String {
        val cart = Cart(userId)
        database.carts.add(cart)
        return cart.cartId
    }

    override fun getCart(cartId: String): Cart? {
        var resultantCart: Cart? = null
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                resultantCart = cart
                break
            }
        }
        return resultantCart
    }

    override fun addToCart(cartId: String, skuId: String) { //product: Product, stock: Stock)
        val cartItem = CartItem(skuId)
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                cart.cartItems.add(cartItem)
                break
            }
        }
    }

    override fun removeFromCart(cartId: String, skuId: String) {
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                val iter = cart.cartItems.iterator()
                for(it in iter) {
                    if(skuId == it.skuId) {
                        iter.remove()
                        break
                    }
                }
                break
            }
        }
    }

    override fun clearCart(cartId: String) {
        val iter = database.carts.iterator()
        for(cart in iter) {
            if(cartId == cart.cartId) {
                cart.cartItems.clear()
            }
        }
    }

    override fun changeItemQuantity(cartId: String, skuId: String, quantity: Int) {
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                for(cartItem in cart.cartItems) {
                    if(skuId == cartItem.skuId) {
                        cartItem.quantity = quantity
                        break
                    }
                }
                break
            }
        }
    }

    override fun updateItemQuantity(cartId: String, skuId: String, quantity: Int) {
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                for(cartItem in cart.cartItems) {
                    if(skuId == cartItem.skuId) {
                        cartItem.quantity -= quantity
                        break
                    }
                }
                break
            }
        }
    }

    override fun updateSubtotal(cartId: String, subTotal: Float) {
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                cart.subTotal = subTotal
            }
        }
    }

    override fun getCartItemQuantity(cartId: String, skuId: String): Int {
        var quantity = 0
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                for(cartItem in cart.cartItems) {
                    if(skuId == cartItem.skuId) {
                        quantity = cartItem.quantity
                        break
                    }
                }
                break
            }
        }
        return quantity
    }

}