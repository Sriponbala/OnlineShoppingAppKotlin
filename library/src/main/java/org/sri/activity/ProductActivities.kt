package org.sri.activity

import org.sri.data.Filter
import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.data.ProductInfo
import org.sri.enums.FilterBy
import org.sri.enums.ProductCategory
import org.sri.enums.StockStatus
import org.sri.interfaces.ProductActivitiesContract
import org.sri.interfaces.ProductsDao
import org.sri.interfaces.UtilityDao

internal class ProductActivities(private val utility: UtilityDao, private val productsDao: ProductsDao): ProductActivitiesContract {

    private lateinit var productsList: MutableList<Pair<Product, StockStatus>>
    private lateinit var filteredProductsList: MutableList<Pair<Product, StockStatus>>
    private var search = false

    init {
        productsDao.addProductDetails()
    }

    override fun getProductsList(): MutableList<Pair<Product, StockStatus>> {
        search = false
        productsList = productsDao.retrieveAllProducts()
        filteredProductsList = productsList
        return productsList
    }

    override fun getProductsList(name: String): MutableList<Pair<Product, StockStatus>> {
        val productName = name.apply {
            this.trim()
            this.lowercase()
        }
        search = true
        return productsList.filter { product ->
            product.first.productName.contains(productName, ignoreCase = true)
        } as MutableList<Pair<Product, StockStatus>>
    }

    override fun getProductsList(category: ProductCategory): MutableList<Pair<Product, StockStatus>> {
        return filteredProductsList.filter { it.first.category == category } as MutableList<Pair<Product, StockStatus>>
    }

    override fun getProductsList(name: String, category: ProductCategory, map: MutableMap<FilterBy, Filter>): MutableList<Pair<Product, StockStatus>> {
        filteredProductsList = if(search) {
            getProductsList(name)
        } else {
            productsList
        }
        filteredProductsList = getProductsList(category)
        map.forEach { (filterBy, filterOption) ->
            filteredProductsList = getFilteredList(category, filterBy, filterOption)
        }
        return filteredProductsList
    }

    override fun clearFilters(name: String): MutableList<Pair<Product, StockStatus>> {
        filteredProductsList = if(search) getProductsList(name) else productsList
        return filteredProductsList
    }

    override fun getProductDetails(skuId: String): Pair<Product, StockStatus>? {
        return productsDao.retrieveProductDetails(skuId)
    }

    override fun getProducts(skuId: String, quantity: Int, lineItems: MutableList<LineItem>): MutableList<ProductInfo> {
        return productsDao.getProducts(skuId, quantity, lineItems)
    }

    override fun getProducts(skuId: String, quantity: Int): MutableList<ProductInfo> {
        return productsDao.getProducts(skuId, quantity)
    }

    override fun getAvailableQuantityOfProduct(skuId: String): Int {
        return if(utility.checkIfProductExists(skuId)) {
            productsDao.retrieveAvailableQuantityOfProduct(skuId)
        } else 0
    }

    private fun getFilteredList(category: ProductCategory, filterOption: FilterBy, finalFilter: Filter): MutableList<Pair<Product, StockStatus>> {
        val filteredList: List<Pair<Product, StockStatus>> = when(category) {
            ProductCategory.BOOK -> {
                when(filterOption) {
                    FilterBy.PRICE -> {
                        val filter : Filter.PriceFilter = finalFilter as Filter.PriceFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.BOOK && it.first.price.toLong() in filter.start .. filter.end }
                    }
                    FilterBy.STATUS -> {
                        val filter : Filter.StatusFilter = finalFilter as Filter.StatusFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.BOOK && it.second.status == filter.status }
                    }
                    FilterBy.BOOKTYPE -> {
                        val filter : Filter.BookTypeFilter = finalFilter as Filter.BookTypeFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.BOOK && (it.first as Product.Book).bookType.type == filter.type }
                    }
                    else -> { emptyList() }
                }
            }
            ProductCategory.MOBILE -> {
                when(filterOption) {
                    FilterBy.PRICE -> {
                        val filter : Filter.PriceFilter = finalFilter as Filter.PriceFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.MOBILE && it.first.price.toLong() in filter.start .. filter.end }
                    }
                    FilterBy.STATUS -> {
                        val filter : Filter.StatusFilter = finalFilter as Filter.StatusFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.MOBILE && it.second.status == filter.status }
                    }
                    FilterBy.BRAND -> {
                        val filter : Filter.BrandFilter = finalFilter as Filter.BrandFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.MOBILE && (it.first as Product.Mobile).brand.brandName == filter.brandName}
                    }
                    else -> { emptyList() }
                }

            }
            ProductCategory.CLOTHING -> {
                when(filterOption) {
                    FilterBy.PRICE -> {
                        val filter : Filter.PriceFilter = finalFilter as Filter.PriceFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.CLOTHING && it.first.price.toLong() in filter.start .. filter.end }
                    }
                    FilterBy.STATUS -> {
                        val filter : Filter.StatusFilter = finalFilter as Filter.StatusFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.CLOTHING && it.second.status == filter.status }
                    }
                    FilterBy.GENDER -> {
                        val filter : Filter.GenderFilter = finalFilter as Filter.GenderFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.CLOTHING && (it.first as Product.Clothing).gender.gender == filter.gender }

                    }
                    FilterBy.COLOUR -> {
                        val filter : Filter.ColourFilter = finalFilter as Filter.ColourFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.CLOTHING && (it.first as Product.Clothing).colour.colour == filter.colour }

                    }
                    else -> { emptyList() }
                }

            }
            ProductCategory.EARPHONE -> {
                when(filterOption) {
                    FilterBy.PRICE -> {
                        val filter : Filter.PriceFilter = finalFilter as Filter.PriceFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.EARPHONE && it.first.price.toLong() in filter.start .. filter.end }
                    }
                    FilterBy.STATUS -> {
                        val filter : Filter.StatusFilter = finalFilter as Filter.StatusFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.EARPHONE && it.second.status == filter.status }
                    }
                    FilterBy.BRAND -> {
                        val filter : Filter.BrandFilter = finalFilter as Filter.BrandFilter
                        filteredProductsList.filter { it.first.category == ProductCategory.EARPHONE && (it.first as Product.Earphone).brand.brandName == filter.brandName}

                    }
                    else -> { emptyList() }
                }
            }
        }
        return filteredList as MutableList<Pair<Product, StockStatus>>
    }

    /*    override fun addProductDetails() {
            productsDao.addProductDetails()
        }*/

    override fun updateStatusOfProduct(lineItem: LineItem) {
        productsDao.updateStatusOfProduct(lineItem)
    }

}