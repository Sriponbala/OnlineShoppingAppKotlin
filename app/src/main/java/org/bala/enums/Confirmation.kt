package org.bala.enums

internal enum class Confirmation(val list: List<String>) {
    CONTINUE(listOf("Continue", "Yes", "Continue selecting filters")),
    GO_BACK(listOf("Go Back", "No", "Stop"))
}