package org.sri.database

import org.sri.data.*
import org.sri.enums.BookType
import org.sri.enums.Brand
import org.sri.enums.Colour
import org.sri.enums.Gender

internal class Database private constructor() {

    companion object {
        private val INSTANCE by lazy { Database() }
        fun getConnection(userName: String, password: String): Database? {
            return if(userName == "root" && password == "tiger") {
                INSTANCE
            } else null
        }
    }

    val users: MutableList<User> = mutableListOf()
    val usersAccountInfo: MutableList<AccountInfo> = mutableListOf()
    val addresses: MutableList<Address> = mutableListOf()
    val usersPassword: MutableList<UserPassword> = mutableListOf()
    val carts: MutableList<Cart> = mutableListOf()
    val usersWishList: MutableList<WishList> = mutableListOf()
    val lineItems: MutableList<LineItem> = mutableListOf()
    val orders: MutableList<Order> = mutableListOf()
    val orderLineItemMappings: MutableList<OrderIdLineItemMapping> = mutableListOf()

    val productSkus: MutableList<Product> = mutableListOf()
    val productsInfo: MutableList<ProductInfo> = mutableListOf()
    val stocks: MutableList<StockAvailability> = mutableListOf()


    private val book1 = Product.Book("BKIPTCON", "Invisible Pain - The cry of nature!!!", 64f, BookType.FICTION)
    private val book2 = Product.Book("BKTPOPT","The Power of Positive Thinking", 153f, BookType.NONFICTION)
    private val book3 = Product.Book("BK400D","400 Days", 133f, BookType.FICTION)
    private val book4 = Product.Book("BKODLWC","One Day, Life Will Change", 133f, BookType.NONFICTION)
    private val book5 = Product.Book("BKPS02","Ponniyin Selvan - Part 2", 442f, BookType.FICTION)


    private val iphone14 =
        Product.Mobile("MIP14","iPhone 14 128GB", 79900f, Brand.APPLE)
    private val samsungGalaxyM33 =
        Product.Mobile("SGM335G","Samsung Galaxy M33 5G", 15499f, Brand.SAMSUNG)
    private val samsungGalaxyS20 =
        Product.Mobile("SGS205G","Samsung Galaxy S20 FE 5G", 29900f, Brand.SAMSUNG)


    private val kurti = Product.Clothing("KRIWN","Kurti", 1500f, Gender.FEMALE, Colour.BLUE)
    private val formalShirt = Product.Clothing("FSMEN","Formal Shirt", 700f, Gender.MALE, Colour.RED)
    private val lehengaCholi = Product.Clothing("LCWN","Lehenga Choli", 3000f, Gender.FEMALE, Colour.RED)
    private val tShirt = Product.Clothing("TSMEN","T-Shirt", 1500f, Gender.MALE, Colour.BLACK)


    private val boAtBassHeads100 = Product.Earphone("BB100","boAt BassHeads 100", 379f, Brand.BOAT)
    private val zebronicsZebBro = Product.Earphone("ZZBRO","ZEBRONICS Zeb-Bro", 149f, Brand.ZEBRONICS)
    private val abortEarbudProTune = Product.Earphone("ABEPT","Abort Earbud Pro Tune", 749f, Brand.GENERICS)

    init {
        productSkus.add(book1)
        productSkus.add(book2)
        productSkus.add(book3)
        productSkus.add(book4)
        productSkus.add(book5)
        productSkus.add(iphone14)
        productSkus.add(samsungGalaxyM33)
        productSkus.add(samsungGalaxyS20)
        productSkus.add(kurti)
        productSkus.add(lehengaCholi)
        productSkus.add(formalShirt)
        productSkus.add(tShirt)
        productSkus.add(boAtBassHeads100)
        productSkus.add(zebronicsZebBro)
        productSkus.add(abortEarbudProTune)

        stocks.add(StockAvailability("BKIPTCON", 3))
        stocks.add(StockAvailability("BKTPOPT", 1))
        stocks.add(StockAvailability("BK400D", 1))
        stocks.add(StockAvailability("BKODLWC", 1))
        stocks.add(StockAvailability("BKPS02", 1))
        stocks.add(StockAvailability("MIP14", 2))
        stocks.add(StockAvailability("SGM335G", 1))
        stocks.add(StockAvailability("SGS205G", 1))
        stocks.add(StockAvailability("KRIWN", 4))
        stocks.add(StockAvailability("FSMEN", 1))
        stocks.add(StockAvailability("LCWN", 1))
        stocks.add(StockAvailability("TSMEN", 1))
        stocks.add(StockAvailability("BB100", 1))
        stocks.add(StockAvailability("ZZBRO", 1))
        stocks.add(StockAvailability("ABEPT", 5))

    }
}