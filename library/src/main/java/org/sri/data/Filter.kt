package org.sri.data

import org.sri.enums.*

sealed class Filter {

    data class PriceFilter(val filter: Price): Filter() {
        val start: Long
        val end: Long

        init {
            when(filter){
                Price.BELOW_100 -> {start = 0; end = 100}
                Price.BETWEEN_100_AND_500 -> {start = 101; end = 499}
                Price.FROM_500_3000 -> {start = 500; end = 3000}
                Price.BETWEEN_3000_AND_15000 -> {start = 3001; end = 14999}
                Price.FROM_15000_TO_40000 -> {start = 15000; end = 40000}
                Price.FROM_40000_TO_100000 -> {start = 40000; end = 100000}
                Price.ABOVE_100000 -> {start = 100001; end = 9_223_372_036_854_775_807}
            }
        }
    }

    data class BrandFilter(val filter: Brand): Filter() {
        val brandName: String = when(filter) {
            Brand.APPLE -> "Apple"
            Brand.BOAT -> "boAt"
            Brand.GENERICS -> "Generics"
            Brand.ZEBRONICS -> "ZEBRONICS"
            Brand.SAMSUNG -> "Samsung"
        }
    }

    data class StatusFilter(val filter: StockStatus): Filter() {
        val status: String = when(filter) {
            StockStatus.INSTOCK -> "In Stock"
            StockStatus.OUTOFSTOCK -> "Out of Stock"
        }
    }

    data class ColourFilter(val filter: Colour): Filter() {
        val colour: String = when(filter) {
            Colour.RED -> "Red"
            Colour.BLUE -> "Blue"
            Colour.BLACK -> "Black"
        }
    }

    data class BookTypeFilter(val filter: BookType): Filter() {
        val type: String = when(filter) {
            BookType.FICTION -> "Fiction"
            BookType.NONFICTION -> "Non Fiction"
        }
    }

    data class GenderFilter(val filter: Gender): Filter() {
        val gender: String = when(filter) {
            Gender.MALE -> "Male"
            Gender.FEMALE -> "Female"
            Gender.OTHERS -> "Others"
        }
    }

}