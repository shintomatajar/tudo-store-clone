package com.tudomart.store.ui.activities.ordersubstitute


import com.google.gson.annotations.SerializedName

data class ModelSubstituteListResponse(
    @SerializedName("data")
    var `data`: List<ModelSubCategoryProductsResponse.Data?>? = listOf(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
)