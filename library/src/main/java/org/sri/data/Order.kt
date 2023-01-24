package org.sri.data

import org.sri.enums.Payment
import org.sri.helper.Helper
import java.time.LocalDate

data class Order(val userId: String, val orderedDate: LocalDate, val shippingAddress: Address, val payment: Payment) {

    val orderId = Helper.generateOrderId()
}