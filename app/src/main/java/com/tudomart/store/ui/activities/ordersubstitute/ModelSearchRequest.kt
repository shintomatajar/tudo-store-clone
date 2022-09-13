package com.tudomart.store.ui.activities.ordersubstitute


import com.google.gson.annotations.SerializedName

data class ModelSearchRequest(
    @SerializedName("arrayStore")
    var arrayStore: List<String?>? = listOf(),
    @SerializedName("intPageLimit")
    var intPageLimit: Int? = 0,
    @SerializedName("intSkipCount")
    var intSkipCount: Int? = 0,
    @SerializedName("strSearchTags")
    var strSearchTags: String? = "",
    var strLoginUserId: String? = "",
    var strSubCategoryId: String? = null
) {
}