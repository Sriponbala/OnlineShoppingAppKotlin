package org.sri.interfaces

import org.sri.data.AccountInfo
import org.sri.data.Address
import org.sri.data.User
import org.sri.enums.AddressField

interface UserAccountActivitiesContract {

        fun getUser(userId: String): Boolean

        fun retrieveUser(): User

        fun getUserId(mobile: String): String

        fun createAndGetUserId(userName: String, userMobile: String, userEmail: String, password: String): String

        fun createAccountInfo(userId: String, cartId: String, wishListId: String): Boolean

        fun getAccountInfo(userId: String): AccountInfo?

        fun getUserAddresses(): List<Address>

        fun getShippingAddress(addressId: String): Address

        fun updateName(name: String): Boolean

        fun updateEmail(email: String): Boolean

        fun addNewAddress(doorNo: String, flatName: String, street: String, area: String, city: String, state: String, pincode: String): Boolean

        fun updateAddress(addressId: String, field: AddressField, value: String)

        fun deleteAddress(addressId: String): Boolean

        fun verifyAccount(mobile: String): Boolean

        fun verifyAccount(mobile: String, password: String): Boolean

}
