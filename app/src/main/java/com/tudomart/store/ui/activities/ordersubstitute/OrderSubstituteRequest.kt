package com.tudomart.store.ui.activities.ordersubstitute


import com.google.gson.annotations.SerializedName

data class OrderSubstituteRequest(
    @SerializedName("arr_OutOfStockProductId")
    val arrOutOfStockProductId: List<String?>?,
    @SerializedName("arr_selected_products")
    val arrSelectedProducts: List<String?>?,
    @SerializedName("strOrderId")
    val strOrderId: String?
)