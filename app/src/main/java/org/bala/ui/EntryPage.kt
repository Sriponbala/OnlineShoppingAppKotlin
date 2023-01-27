package org.bala.ui

import org.bala.enums.Entry
import org.bala.helper.IOHandler
import org.bala.utils.Navigator

internal class EntryPage {

    fun openEntryPage(navigator: Navigator) {
        println("-----ONLINE SHOPPING APPLICATION-----\n")
        val entry = Entry.values()
        while(true) {
            IOHandler.showMenu("ENTRY PAGE", entry)
            when(IOHandler.getUserChoice(entry)) {
                Entry.VIEW_APP -> {
                    navigator.goToHomePage(navigator)
                }
                Entry.SIGN_UP -> {
                    navigator.goToSignUpPage(navigator)
                }
                Entry.SIGN_IN -> {
                    navigator.goToSignInPage(navigator)
                }
                Entry.EXIT -> {
                    println("Thank You! Visit again!")
                    break
                }
            }
        }
    }

}