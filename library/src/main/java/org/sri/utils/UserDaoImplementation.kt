package org.sri.utils

import org.sri.data.AccountInfo
import org.sri.data.Address
import org.sri.data.User
import org.sri.data.UserPassword
import org.sri.database.Database
import org.sri.enums.AddressField
import org.sri.interfaces.UserDao

internal class UserDaoImplementation private constructor(private val userName: String = "root",
                                   private val password: String = "tiger"): UserDao {

    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { UserDaoImplementation() }
        fun getInstance(): UserDaoImplementation {
            return INSTANCE
        }
    }

    override fun createAndGetUserId(userName: String, userMobile: String, userEmail: String, password: String): String {
        val user = User(userName, userMobile,userEmail)
        database.users.add(user)
        val userPassword = UserPassword(user.userId, password)
        database.usersPassword.add(userPassword)
        return user.userId
    }

    override fun createUserAccountInfo(userId: String, cartId: String, wishListId: String) {
        val accountInfo = AccountInfo(userId, cartId, wishListId)
        database.usersAccountInfo.add(accountInfo)
    }

    override fun retrieveAccountInfo(userId: String): AccountInfo? {
        var accountInfo: AccountInfo? = null
        for(userAccountInfo in database.usersAccountInfo) {
            if(userId == userAccountInfo.userId) {
                accountInfo = userAccountInfo
            }
        }
        return accountInfo
    }

    override fun retrieveUserId(mobile: String): String {
        var id = ""
        for(user in database.users) {
            if(mobile == user.userMobile) {
                id = user.userId
                break
            }
        }
        return id
    }

    override fun retrieveUser(userId: String): User? {
        var activeUser: User? = null
        for(user in database.users) {
            if(userId == user.userId) {
                activeUser = user
            }
        }
        return activeUser
    }

    override fun addAddress(address: Address) {
        database.addresses.add(address)
    }

    override fun updateName(userId: String, name: String) {
        for(user in database.users) {
            if(userId == user.userId) {
                user.userName = name
            }
        }
    }

    override fun updateEmail(userId: String, email: String) {
        for(user in database.users) {
            if(userId == user.userId) {
                user.userEmail = email
            }
        }
    }

    override fun getUserAddresses(userId: String): MutableList<Address> {
        val addresses: MutableList<Address> = mutableListOf()
        for(address in database.addresses) {
            if(userId == address.userId) {
                addresses.add(address)
            }
        }
        return addresses
    }

    override fun deleteAddress(addressId: String) {
        for(address in database.addresses) {
            if(addressId == address.addressId) {
                database.addresses.remove(address)
                break
            }
        }
    }

    override fun updateAddress(addressId: String, field: AddressField, value: String) {
        when (field) {
            AddressField.DOORNUMBER -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.doorNo = value
                        break
                    }
                }
            }

            AddressField.FLATNAME -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.flatName = value
                        break
                    }
                }
            }

            AddressField.STREET -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.street = value
                        break
                    }
                }
            }

            AddressField.AREA -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.area = value
                        break
                    }
                }
            }

            AddressField.CITY -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.city = value
                        break
                    }
                }
            }

            AddressField.STATE -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.state = value
                        break
                    }
                }
            }

            AddressField.PINCODE -> {
                for (address in database.addresses) {
                    if (addressId == address.addressId) {
                        address.pincode = value
                        break
                    }
                }
            }

            AddressField.BACK -> {}
        }
    }

}