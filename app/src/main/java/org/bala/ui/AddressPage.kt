package org.bala.ui

import org.bala.enums.AddressManagement
import org.bala.enums.AddressSelection
import org.bala.helper.DashboardServices
import org.bala.helper.IOHandler
import org.sri.data.Address
import org.sri.enums.AddressField
import org.sri.interfaces.UserAccountActivitiesContract

class AddressPage(private val userAccountActivities: UserAccountActivitiesContract): DashboardServices {

    private lateinit var addresses: List<Address>
    private var doorNo = ""
    private var flatName = ""
    private var street = ""
    private var area = ""
    private var city = ""
    private var state = ""
    private var pincode = ""
    private var selectAddress = false
    private var shippingAddress: Address? = null

    fun setSelectAddress(input: Boolean) {
        this.selectAddress = input
    }

    fun selectAddressForDelivery(): Address? {
        do{
            openAddressPage()
        } while(shippingAddress == null && selectAddress)
        return shippingAddress
    }

    fun deselectShippingAddress() {
        this.shippingAddress = null
    }

    fun openAddressPage() {
        this.addresses = userAccountActivities.getUserAddresses()
        displayAllAddresses()
        val addressSelectionOptions: Array<AddressSelection> = if(selectAddress) {
            AddressSelection.values()
        } else {
            arrayOf(AddressSelection.ADD_NEW_ADDRESS, AddressSelection.SELECT_FROM_SAVED_ADDRESS, AddressSelection.GO_BACK)
        }
        while(true) {
            super.showDashboard("ADDRESS PAGE DASHBOARD", addressSelectionOptions)
            when(super.getUserChoice(addressSelectionOptions)) {

                AddressSelection.ADD_NEW_ADDRESS -> {
                    getUserInputs()
                    if(IOHandler.confirm()) {
                        addNewAddress()
                    }
                }

                AddressSelection.SELECT_FROM_SAVED_ADDRESS -> {
                    this.addresses = userAccountActivities.getUserAddresses()
                    while(true) {
                        displayAllAddresses()
                        if(selectAddress) {
                            if(addresses.isEmpty()) {
                                break
                            } else{
                                shippingAddress = userAccountActivities.getShippingAddress(selectAnAddress())
                                break
                            }
                        }
                        if(manageAddress()) {
                            break
                        }
                    }
                }

                AddressSelection.GO_BACK -> {
                    selectAddress = false
                    break
                }

                AddressSelection.NEXT -> {
                    if(shippingAddress == null) {
                        println("Select an address to proceed!")
                    } else {
                        selectAddress = false
                        break
                    }
                }
            }
        }
    }

    private fun displayAllAddresses() {
        if(this.addresses.isEmpty()) {
            println("        No address found       ")
        } else {
            var sno = 1
            println("-----------------YOUR ADDRESSES------------------")
            for(address in addresses) {
                println("""${sno++}. ${address.doorNo}, ${address.flatName},
                    |   ${address.street}, ${address.area},
                    |   ${address.city}, ${address.state} - ${address.pincode}
                """.trimMargin())
            }
        }
    }

    private fun addNewAddress() {
        if(userAccountActivities.addNewAddress(doorNo, flatName, street, area, city, state, pincode)) {
            println("Address added!")
        } else {
            println("Failed to add address!")
        }
    }

    private fun manageAddress(): Boolean {
        // delete or edit
        if(addresses.isEmpty()) {
            return true
        } else {
            val addressId = selectAnAddress()
            val addressManagementOptions = AddressManagement.values()
            while(true) {
                super.showDashboard("ADDRESS MANAGEMENT OPTIONS", addressManagementOptions)
                return when(super.getUserChoice(addressManagementOptions)) {
                    AddressManagement.EDIT -> { // edit address
                        editAddress(addressId)
                        false
                    }
                    AddressManagement.DELETE -> { // delete address
                        deleteAddress(addressId)
                        true
                    }
                    AddressManagement.BACK ->  true
                }
            }
        }
    }

    private fun editAddress(addressId: String) {
        val addressFields = AddressField.values()
        while(true) {
            super.showDashboard("ADDRESS FIELDS", addressFields)
            when(super.getUserChoice(addressFields)) {
                AddressField.DOORNUMBER -> {
                    userAccountActivities.updateAddress(addressId, AddressField.DOORNUMBER, getUserInput("door number"))
                }
                AddressField.FLATNAME -> {
                    userAccountActivities.updateAddress(addressId, AddressField.FLATNAME, getUserInput("flat name"))
                }
                AddressField.STREET -> {
                    userAccountActivities.updateAddress(addressId, AddressField.STREET, getUserInput("street name"))
                }
                AddressField.AREA -> {
                    userAccountActivities.updateAddress(addressId, AddressField.AREA, getUserInput("area name"))
                }
                AddressField.CITY -> {
                    userAccountActivities.updateAddress(addressId, AddressField.CITY, getUserInput("city name"))
                }
                AddressField.STATE -> {
                    userAccountActivities.updateAddress(addressId, AddressField.STATE, getUserInput("state name"))
                }
                AddressField.PINCODE -> {
                    userAccountActivities.updateAddress(addressId, AddressField.PINCODE, getPincode())
                }
                AddressField.BACK -> {
                    break
                }
            }
        }
    }

    private fun deleteAddress(addressId: String) {
        if(userAccountActivities.deleteAddress(addressId)) {
            println("Address deleted!")
        } else println("Failed to delete address!")
    }

    private fun selectAnAddress(): String {
        var option: Int
        var selectedAddress: String
        while(true){
            println("SELECT AN ADDRESS: ")
            try{
                val userInput = readLine()!!
                option = userInput.toInt()
                if(IOHandler.checkValidRecord(option,addresses.size)) {
                    selectedAddress = addresses[option - 1].addressId
                    break
                } else {
                    println("Invalid option! Try again")
                }
            } catch(exception: Exception) {
                println("Invalid option! Try again!")
            }
        }
        return selectedAddress
    }


    private fun getUserInputs() {
        println("FILL ADDRESS FIELDS: ")

        do{
            println("ENTER DOOR NUMBER: [Should not be empty]")
            doorNo = readLine()!!
        } while(IOHandler.fieldValidation(doorNo) || !IOHandler.validateAddressFields(doorNo))

        do{
            println("ENTER FLAT NAME: [Should not be empty]")
            flatName = readLine()!!
        } while(IOHandler.fieldValidation(flatName) || !IOHandler.validateAddressFields(flatName))

        do{
            println("ENTER STREET NAME: [Should not be empty]")
            street = readLine()!!
        } while(IOHandler.fieldValidation(street) || !IOHandler.validateAddressFields(street))

        do{
            println("ENTER AREA NAME: [Should not be empty]")
            area = readLine()!!
        } while(IOHandler.fieldValidation(area) || !IOHandler.validateAddressFields(area))

        do{
            println("ENTER CITY NAME: [Should not be empty]")
            city = readLine()!!
        } while(IOHandler.fieldValidation(city) || !IOHandler.validateAddressFields(city))

        do{
            println("ENTER STATE NAME: [Should not be empty]")
            state = readLine()!!
        } while(IOHandler.fieldValidation(state) || !IOHandler.validateAddressFields(state))

        do{
            println("""ENTER PINCODE: 
                |[Should not be empty,
                |Should start with number > 0,
                |must contain 6 digits,
                |Format: 600062 OR 600 062]
            """.trimMargin())
            pincode = readLine()!!
        } while(IOHandler.fieldValidation(pincode) || !IOHandler.validatePincode(pincode))
    }

    private fun getUserInput(message: String = ""): String {
        var userInput: String
        do {
            println("ENTER ${message.uppercase()}: [Should not be empty]")
            userInput = readLine()!!
        } while(IOHandler.fieldValidation(userInput) || !IOHandler.validateAddressFields(userInput))
        return userInput
    }

    private fun getPincode(): String {
        var pincode: String
        do {
            println("""ENTER PINCODE: 
                |[Should not be empty,
                |Should start with number > 0,
                |must contain 6 digits,
                |Format: 600062 OR 600 062]
            """.trimMargin())
            pincode = readLine()!!
        } while(IOHandler.fieldValidation(pincode) || !IOHandler.validatePincode(pincode))
        return pincode
    }

}