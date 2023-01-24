package org.bala.helper

import org.bala.enums.Confirmation
import java.time.LocalDate
import java.util.regex.Pattern
import kotlin.random.Random

internal object IOHandler {

    fun confirm(): Boolean {
        while(true) {
            println("Confirm: ")
            for (option in Confirmation.values()) {
                println("${option.ordinal + 1}. $option")
            }
            try {
                val choice = readLine()!!
                val confirmationChoice = choice.toInt() // chance of exception
                if(checkValidRecord(confirmationChoice, Confirmation.values().size)) {
                    return when(Confirmation.values()[confirmationChoice - 1]) {
                        Confirmation.CONTINUE -> true
                        Confirmation.GO_BACK -> false
                    }
                } else {
                    println("Enter proper input: ")
                }
            } catch(exception: Exception) {
                println("Enter valid option!")
            }
        }
    }

    fun fieldValidation(fieldValue: String): Boolean {
        return fieldValue == ""
    }

    fun checkValidRecord(option: Int, size: Int): Boolean {
        return option != 0 && option <= size
    }

    fun generateOTP(): String {
        return Random.nextInt(100000,1000000).toString()
    }

    fun verifyOtp(currentOtp: String, generatedOtp: String): Boolean {
        return currentOtp == generatedOtp
    }

    fun confirmPassword(confirmPassword: String, password: String): Boolean {
        return confirmPassword == password
    }

    fun validateMobileNumber(number: String): Boolean { // 10-digit Phone number with Country Code Prefix(max 3 characters)
        return Pattern.matches("^\\d{10}$", number)
    }

    fun validateEmail(email: String): Boolean {
        return Pattern.matches("^[a-z0-9_!#$.-]{3,30}+@[a-z]{3,20}+.[a-z]{2,3}+\$", email)
    }

    fun validatePasswordPattern(password: String): Boolean {
        return Pattern.matches("^[a-zA-Z0-9!#@$%^&*_+`~]{4,8}+$", password)
    }

    fun validatePincode(pincode: String): Boolean {
        return Pattern.matches("^[1-9][0-9]{2}\\s?[0-9]{3}$", pincode)
    }

    fun validateAddressFields(fieldValue: String): Boolean {
        return Pattern.matches("^[a-zA-Z1-9][a-zA-Z0-9-.\\s]{0,30}$", fieldValue)
    }

    fun generateOrderedDate(): LocalDate {
        return LocalDate.of(2022, 11, 18).plusDays(Random.nextLong(91))
    }

}