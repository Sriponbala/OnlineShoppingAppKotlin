package org.sri.data

import org.sri.enums.StockStatus
import org.sri.helper.Helper

data class ProductInfo(val skuId: String, var status: StockStatus) {
    val productId: String = Helper.generateProductId()
}