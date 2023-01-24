package org.sri.utils

import org.sri.data.LineItem
import org.sri.data.Product
import org.sri.data.ProductInfo
import org.sri.database.Database
import org.sri.enums.StockStatus
import org.sri.interfaces.ProductsDao

internal class ProductsDaoImplementation private constructor(private val userName: String = "root",
                                       private val password: String = "tiger"): ProductsDao {

    private val database: Database = Database.getConnection(this.userName, this.password)!!

    companion object {
        private val INSTANCE by lazy { ProductsDaoImplementation() }
        fun getInstance(): ProductsDaoImplementation {
            return INSTANCE
        }
    }

    override fun retrieveAllProducts(): MutableList<Pair<Product, StockStatus>> {
        val productsList: MutableList<Pair<Product, StockStatus>> = mutableListOf()
        for(productSku in database.productSkus) {
            if(productsList.isEmpty()) {
                var status: StockStatus = StockStatus.OUTOFSTOCK
                for(productInfo in database.productsInfo) {
                    if(productSku.skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                        status = StockStatus.INSTOCK
                        break
                    }
                }
                val productInfo = Pair(productSku, status)
                productsList.add(productInfo)
            } else {
                var isProductAlreadyEntered = false
                for(product in productsList) {
                    if(product.first.skuId == productSku.skuId) {
                        isProductAlreadyEntered = true
                        break
                    }
                }
                if(!isProductAlreadyEntered) {
                    var status: StockStatus = StockStatus.OUTOFSTOCK
                    for(productInfo in database.productsInfo) {
                        if(productSku.skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                            status = StockStatus.INSTOCK
                            break
                        }
                    }
                    val productInfo = Pair(productSku, status)
                    productsList.add(productInfo)
                }
            }
        }
        return productsList
    }

    override fun retrieveAvailableQuantityOfProduct(skuId: String): Int {
        var availableQuantity = 0
        for(productInfo in database.productsInfo) {
            if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                availableQuantity += 1
            }
        }
        return availableQuantity
    }

    override fun updateStatusOfProduct(lineItem: LineItem) {
        for(productInfo in database.productsInfo) {
            if(lineItem.skuId == productInfo.skuId && lineItem.productId == productInfo.productId) {
                productInfo.status = StockStatus.OUTOFSTOCK
                updateAvailableQuantityOfProduct(lineItem.skuId)
            }
        }
    }

    /*    override fun retrieveAProduct(skuId: String): ProductInfo? {
            var productData: ProductInfo? = null
            for(productInfo in database.productsInfo) {
                if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                    productData = productInfo
                    break
                }
            }
            return productData
        }*/

    override fun retrieveProductDetails(skuId: String): Pair<Product, StockStatus>? {
        var productSku: Pair<Product, StockStatus>? = null
        for(localProductSku in database.productSkus) {
            if(skuId == localProductSku.skuId) {
                for(productInfo in database.productsInfo) {
                    if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                        productSku = Pair(localProductSku, StockStatus.INSTOCK)
                        break
                    } else {
                        productSku = Pair(localProductSku, StockStatus.OUTOFSTOCK)
                    }
                }
                break
            }
        }
        return productSku
    }

    override fun addProductDetails() {
        var count = 0
        for (productSku in database.productSkus) {
            val availableQuantity = getAvailableQuantityOfProduct(productSku.skuId)
            while(count < availableQuantity) {
                val productInfo = ProductInfo(productSku.skuId, StockStatus.INSTOCK)
                database.productsInfo.add(productInfo)
                count++
            }
            count = 0
        }
    }

    private fun getAvailableQuantityOfProduct(skuId: String): Int {
        var availableQuantity = 0
        for(stock in database.stocks) {
            if(skuId == stock.skuId) {
                availableQuantity = stock.availableQuantity
                break
            }
        }
        return availableQuantity
    }

    private fun updateAvailableQuantityOfProduct(skuId: String) {
        for(stock in database.stocks) {
            if(skuId == stock.skuId) {
                stock.availableQuantity -= 1
                break
            }
        }
    }

    override fun getProducts(skuId: String, quantity: Int): MutableList<ProductInfo> {
        val products = mutableListOf<ProductInfo>()
        var count = 0
        for(productInfo in database.productsInfo) {
            var present = false
            if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                if(count == quantity) {
                    break
                } else {
                    if(products.isEmpty()) {
                        products.add(productInfo)
                        count++
                    } else {
                        for(product in products) {
                            if(productInfo.productId == product.productId) {
                                present = true
                                break
                            }
                        }
                        if(!present) {
                            products.add(productInfo)
                            count++
                        }
                    }
                }
            }
        }
        return products
    }

    override fun getProducts(skuId: String, quantity: Int, lineItems: MutableList<LineItem>): MutableList<ProductInfo> {
        val products = mutableListOf<ProductInfo>()
        var count = 0
        for(productInfo in database.productsInfo) {
            var present = false
            if(lineItems.isEmpty()) {
                if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                    products.add(productInfo)
                    count++
                }
            }
            if(count == quantity) {
                break
            } else {
                if(skuId == productInfo.skuId && productInfo.status == StockStatus.INSTOCK) {
                    for(lineItem in lineItems) {
                        if(lineItem.productId == productInfo.productId) {
                            present = true
                            break
                        }
                    }
                    if(!present) {
                        if(products.isEmpty()) {
                            products.add(productInfo)
                            count++
                        } else {
                            for(product in products) {
                                if(product.productId != productInfo.productId) {
                                    products.add(productInfo)
                                    count++
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
        return products
    }

}