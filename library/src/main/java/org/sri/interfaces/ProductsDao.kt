package org.sri.interfaces

import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.data.ProductInfo
import org.sri.enums.StockStatus

internal interface ProductsDao {

    fun retrieveAllProducts(): MutableList<Pair<Product, StockStatus>>

    fun retrieveAvailableQuantityOfProduct(skuId: String): Int

    fun updateStatusOfProduct(lineItem: LineItem)

    fun retrieveProductDetails(skuId: String): Pair<Product, StockStatus>?

    fun addProductDetails()

    fun getProducts(skuId: String, quantity: Int, lineItems: MutableList<LineItem>): MutableList<ProductInfo>

    fun getProducts(skuId: String, quantity: Int): MutableList<ProductInfo>

}