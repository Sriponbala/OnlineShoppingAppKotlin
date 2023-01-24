package org.sri.data

import org.sri.helper.Helper

data class Address(var doorNo: String, var flatName: String, var street: String, var area: String, var city: String, var state: String, var pincode: String, val userId: String) {
    val addressId: String = Helper.generateAddressId()
}