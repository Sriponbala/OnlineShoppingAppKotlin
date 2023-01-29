package org.sri.interfaces

import org.sri.data.Filter
import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.data.ProductInfo
import org.sri.enums.CommonFilter
import org.sri.enums.FilterBy
import org.sri.enums.ProductCategory
import org.sri.enums.StockStatus

interface ProductActivitiesContract {

    fun getProductsList(): MutableList<Pair<Product, StockStatus>>

    fun getProductsList(name: String): MutableList<Pair<Product, StockStatus>>

    fun retrieveProductsList(category: ProductCategory, commonFiltersMap: MutableMap<CommonFilter, Filter>): MutableList<Pair<Product, StockStatus>>

    fun retrieveProductsList(commonFiltersMap: MutableMap<CommonFilter, Filter>): MutableList<Pair<Product, StockStatus>>

    fun getProductsList(category: ProductCategory, map: MutableMap<FilterBy, Filter>): MutableList<Pair<Product, StockStatus>>

    fun getProductsList(category: ProductCategory): MutableList<Pair<Product, StockStatus>>

    fun getProductsList(name: String, category: ProductCategory, map: MutableMap<FilterBy, Filter>): MutableList<Pair<Product, StockStatus>>

    fun clearFilters(name: String): MutableList<Pair<Product, StockStatus>>

    fun getProductDetails(skuId: String): Pair<Product, StockStatus>?

    fun getProducts(skuId: String, quantity: Int, lineItems: MutableList<LineItem>): MutableList<ProductInfo>

    fun getProducts(skuId: String, quantity: Int): MutableList<ProductInfo>

    fun getAvailableQuantityOfProduct(skuId: String): Int

    fun updateStatusOfProduct(lineItem: LineItem)
}