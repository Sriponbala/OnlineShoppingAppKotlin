package org.sri.utils

import org.sri.data.Cart
import org.sri.data.User
import org.sri.database.Database
import org.sri.interfaces.UtilityDao

internal class UtilityDaoImplementation private constructor(private val userName: String = "root",
                                  private val password: String = "tiger"): UtilityDao {

    private lateinit var users: MutableList<User>
    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { UtilityDaoImplementation() }
        fun getInstance(): UtilityDaoImplementation {
            return INSTANCE
        }
    }

    override fun checkUniqueUser(mobile: String): Boolean {
        this.users = database.users
        var flag = false
        if (users.isEmpty()) {
            flag = true
        } else {
            for(user in users) {
                if(mobile == user.userMobile) {
                    flag = false
                    break
                } else {
                    flag = true
                }
            }
        }
        return flag
    }

    override fun validateLoginCredentials(mobile: String, password: String): Boolean {
        this.users = database.users
        var flag = false
        for(user in users) {
            if(mobile == user.userMobile && password == getPassword(user.userId)) {
                flag = true
                break
            }
        }
        return flag
    }

    private fun getPassword(userId: String): String {
        var password = ""
        for(userPassword in database.usersPassword) {
            if(userId == userPassword.userId) {
                password = userPassword.password
            }
        }
        return password
    }

    override fun checkIfUserExists(userId: String): Boolean {
        var flag = false
        for(user in this.users) {
            if(userId == user.userId) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun checkIfUserAccountInfoExists(userId: String): Boolean {
        var flag = false
        for(userAccount in database.usersAccountInfo) {
            if(userId == userAccount.userId) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun checkIfWishListExists(wishListId: String): Boolean {
        var flag = false
        for(wishList in database.usersWishList) {
            if(wishListId == wishList.wishListId) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun checkIfProductIsInUserWishList(wishListId: String, skuId: String): Boolean {
        var isProductInWishList = false
        if(checkIfWishListExists(wishListId)) {
            for(wishList in database.usersWishList) {
                if(wishListId == wishList.wishListId) {
                    for(wishListItem in wishList.wishListItems) {
                        if(skuId == wishListItem.skuId) {
                            isProductInWishList = true
                            break
                        }
                    }
                    break
                }
            }
        }
        return isProductInWishList
    }

    override fun checkIfProductExists(skuId: String): Boolean {
        var isProductExists = false
        for(productSku in database.productSkus) {
            if(skuId == productSku.skuId) {
                isProductExists = true
                break
            }
        }
        return isProductExists
    }

    override fun checkIfCartExists(cartId: String): Boolean {
        var flag = false
        for(cart in database.carts) {
            if(cartId == cart.cartId) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun checkIfItemIsInCart(cart: Cart, skuId: String): Boolean {
        var isItemInCart = false
        for(cartItem in cart.cartItems) {
            if(skuId == cartItem.skuId) {
                isItemInCart = true
                break
            }
        }
        return isItemInCart
    }

    override fun checkIfAddressExists(addressId: String): Boolean {
        var flag = false
        for(address in database.addresses) {
            if(addressId == address.addressId) {
                flag = true
                break
            }
        }
        return flag
    }

}