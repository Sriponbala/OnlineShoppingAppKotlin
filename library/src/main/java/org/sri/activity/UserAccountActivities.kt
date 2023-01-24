package org.sri.activity

import org.sri.data.AccountInfo
import org.sri.data.Address
import org.sri.data.User
import org.sri.enums.AddressField
import org.sri.interfaces.UserAccountActivitiesContract
import org.sri.interfaces.UserDao
import org.sri.interfaces.UtilityDao

internal class UserAccountActivities(private val utility: UtilityDao, private val userDao: UserDao): UserAccountActivitiesContract {

    private var user: User? = null
    private lateinit var addressesList: List<Address>

    override fun getUser(userId: String): Boolean {
        return if(utility.checkIfUserExists(userId)) {
            this.user = userDao.retrieveUser(userId)
            this.user != null
        } else false
    }

    override fun retrieveUser(): User {
        return user!!
    }

    override fun getUserId(mobile: String): String {
        return userDao.retrieveUserId(mobile)
    }

    override fun createAndGetUserId(userName: String, userMobile: String, userEmail: String, password: String): String {
        return userDao.createAndGetUserId(userName, userMobile, userEmail, password)
    }

    override fun createAccountInfo(userId: String, cartId: String, wishListId: String): Boolean {
        return if(utility.checkIfUserExists(userId)) {
            userDao.createUserAccountInfo(userId, cartId, wishListId)
            true
        } else false
    }

    override fun getAccountInfo(userId: String): AccountInfo? {
        val accountInfo: AccountInfo? = if(utility.checkIfUserExists(userId)) {
            if(utility.checkIfUserAccountInfoExists(userId)) {
                userDao.retrieveAccountInfo(userId)
            } else null
        } else null
        return accountInfo
    }

    override fun getUserAddresses(): List<Address> {
        addressesList = userDao.getUserAddresses(user!!.userId)
        return addressesList
    }

    override fun getShippingAddress(addressId: String): Address {
        lateinit var selectedAddress: Address
        for(address in addressesList) {
            if(addressId == address.addressId) {
                selectedAddress = address
                break
            }
        }
        return selectedAddress
    }

    override fun updateName(name: String): Boolean {
        return if(utility.checkIfUserExists(user!!.userId)) {
            userDao.updateName(user!!.userId, name)
            true
        } else false
    }

    override fun updateEmail(email: String): Boolean {
        return if(utility.checkIfUserExists(user!!.userId)) {
            userDao.updateEmail(user!!.userId, email)
            true
        } else false
    }

    override fun addNewAddress(doorNo: String, flatName: String, street: String, area: String, city: String, state: String, pincode: String): Boolean {
        return if(utility.checkIfUserExists(user!!.userId)) {
            val address = Address(doorNo, flatName, street, area, city, state, pincode, user!!.userId)
            userDao.addAddress(address)
            true
        } else false
    }

    override fun updateAddress(addressId: String, field: AddressField, value: String) {
        if(utility.checkIfUserExists(user!!.userId)) {
            if(utility.checkIfAddressExists(addressId)) {
                when(field) {
                    AddressField.DOORNUMBER -> {
                        userDao.updateAddress(addressId, AddressField.DOORNUMBER, value)
                    }
                    AddressField.FLATNAME -> {
                        userDao.updateAddress(addressId, AddressField.FLATNAME, value)
                    }
                    AddressField.STREET -> {
                        userDao.updateAddress(addressId, AddressField.STREET, value)
                    }
                    AddressField.AREA -> {
                        userDao.updateAddress(addressId, AddressField.AREA, value)
                    }
                    AddressField.CITY -> {
                        userDao.updateAddress(addressId, AddressField.CITY, value)
                    }
                    AddressField.STATE -> {
                        userDao.updateAddress(addressId, AddressField.STATE, value)
                    }
                    AddressField.PINCODE -> {
                        userDao.updateAddress(addressId, AddressField.PINCODE, value)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun deleteAddress(addressId: String): Boolean {
        return if(utility.checkIfUserExists(user!!.userId)) {
            if(utility.checkIfAddressExists(addressId)) {
                userDao.deleteAddress(addressId)
                true
            } else false
        } else false
    }

    override fun verifyAccount(mobile: String): Boolean {
        return utility.checkUniqueUser(mobile)
    }

    override fun verifyAccount(mobile: String, password: String): Boolean {
        return utility.validateLoginCredentials(mobile, password)
    }

}