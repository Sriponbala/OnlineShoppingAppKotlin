package org.bala.ui

import org.bala.enums.AddressManagement
import org.bala.enums.AddressSelection
import org.bala.helper.IOHandler
import org.sri.data.Address
import org.sri.enums.AddressField
import org.sri.interfaces.UserAccountActivitiesContract

internal class AddressPage(private val userAccountActivities: UserAccountActivitiesContract) {

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
            IOHandler.showMenu("ADDRESS PAGE MENU", addressSelectionOptions)
            when(IOHandler.getUserChoice(addressSelectionOptions)) {

                AddressSelection.ADD_NEW_ADDRESS -> {
                    getUserInputs()
                    if(IOHandler.confirm(1)) {
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
                IOHandler.showMenu("ADDRESS MANAGEMENT OPTIONS", addressManagementOptions)
                return when(IOHandler.getUserChoice(addressManagementOptions)) {
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
            IOHandler.showMenu("ADDRESS FIELDS", addressFields)
            when(IOHandler.getUserChoice(addressFields)) {
                AddressField.DOORNUMBER -> {
                    userAccountActivities.updateAddress(addressId, AddressField.DOORNUMBER, IOHandler.readAddressField("door number"))
                }
                AddressField.FLATNAME -> {
                    userAccountActivities.updateAddress(addressId, AddressField.FLATNAME, IOHandler.readAddressField("flat name"))
                }
                AddressField.STREET -> {
                    userAccountActivities.updateAddress(addressId, AddressField.STREET, IOHandler.readAddressField("street name"))
                }
                AddressField.AREA -> {
                    userAccountActivities.updateAddress(addressId, AddressField.AREA, IOHandler.readAddressField("area name"))
                }
                AddressField.CITY -> {
                    userAccountActivities.updateAddress(addressId, AddressField.CITY, IOHandler.readAddressField("city name"))
                }
                AddressField.STATE -> {
                    userAccountActivities.updateAddress(addressId, AddressField.STATE, IOHandler.readAddressField("state name"))
                }
                AddressField.PINCODE -> {
                    userAccountActivities.updateAddress(addressId, AddressField.PINCODE, IOHandler.readPincode())
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
        val option = IOHandler.readOption("address", addresses.size)
        return addresses[option - 1].addressId
    }

    private fun getUserInputs() {
        println("FILL ADDRESS FIELDS: ")
        doorNo = IOHandler.readAddressField("door number")
        flatName = IOHandler.readAddressField("flat name")
        street = IOHandler.readAddressField("street name")
        area = IOHandler.readAddressField("area name")
        city = IOHandler.readAddressField("city name")
        state = IOHandler.readAddressField("state name")
        pincode = IOHandler.readPincode()
    }

}