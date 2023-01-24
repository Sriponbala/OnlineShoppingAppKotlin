package org.bala.enums

enum class Confirmation(val list: List<String>) {
    CONTINUE(listOf("Continue", "Yes", "Continue selecting filters")),
    GO_BACK(listOf("Back", "Go Back", "Stop"))
}