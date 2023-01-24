package org.bala.helper

interface DashboardServices {

    fun <E: Enum<E>> showDashboard(title: String, enumArray: Array<E>) {
        var sno = 1
        println("-------------${title.uppercase()}-------------")
        for(element in enumArray) {
            println("${sno++}. $element")
        }
    }

    fun <E: Enum<E>> getUserChoice(enumArray: Array<E>): E {

        while (true) {
            try {
                println("Enter your choice: ")
                val option = readLine()!!
                val dashBoardOption = option.toInt()
                if(IOHandler.checkValidRecord(dashBoardOption, enumArray.size)) {
                    return enumArray[dashBoardOption-1]
                } else {
                    println("Enter valid option!")
                }
            } catch (exception: Exception) {
                println("Enter valid option!")
            }
        }
    }

}