package ae.tudomart.store.ui.activities.ordersubstitute


import com.google.gson.annotations.SerializedName

data class ModelSubCategoryProductsResponse(
    @SerializedName("count")
    var count: Int? = 0,
    @SerializedName("data")
    var `data`: List<Data?>? = listOf(),
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
) {
    data class Data(
        var isSelected: Boolean = false,
        @SerializedName("arrayOtherImages")
        var arrayOtherImages: List<ArrayOtherImage?>? = listOf(),
//        @SerializedName("arrayProductDetail")
//        var arrayProductDetail: List<ArrayProductDetail?>? = listOf(),
        @SerializedName("arrayThumbnail")
        var arrayThumbnail: List<ArrayThumbnail?>? = listOf(),
        @SerializedName("blnStockAvailability")
        var blnStockAvailability: Boolean? = false,
        @SerializedName("intDiscount")
        var intDiscount: Double? = 0.0,
        @SerializedName("intMRP")
        var intMRP: Double? = 0.0,
        @SerializedName("intMinimumAmount")
        var intMinimumAmount: Double? = 0.0,
        @SerializedName("strDisclaimer")
        var strDisclaimer: String? = "",
        @SerializedName("blnExpress")
        var blnExpress: Boolean? = false,
        @SerializedName("intMaxQuantity")
        var intMaxQuantity: Int? = 0,
        @SerializedName("intMinQuantity")
        var intMinQuantity: Int? = 0,
        @SerializedName("intSellingPrice")
        var intSellingPrice: Double? = 0.0,
        @SerializedName("objShop")
        var objShop: ObjShop? = ObjShop(),
        @SerializedName("objSubCategory")
        var objSubCategory: ObjSubCategory? = ObjSubCategory(),
        @SerializedName("pkProductId")
        var pkProductId: String? = "",
        @SerializedName("strDescription")
        var strDescription: String? = "",
        @SerializedName("strProductName")
        var strProductName: String? = "",
        var quantity : Int = 0
    ) {
        data class ArrayOtherImage(
            @SerializedName("imageName")
            var imageName: String? = "",
            @SerializedName("imageUrl")
            var imageUrl: String? = ""
        )

        data class ArrayProductDetail(
            @SerializedName("key")
            var key: String? = "",
            @SerializedName("value")
            var value: String? = ""
        )

        data class ArrayThumbnail(
            @SerializedName("imageName")
            var imageName: String? = "",
            @SerializedName("imageUrl")
            var imageUrl: String? = ""
        )

        data class ObjShop(
            @SerializedName("fkShopId")
            var fkShopId: String? = "",
            @SerializedName("strImageUrl")
            var strImageUrl: String? = "",
            @SerializedName("strPlace")
            var strPlace: String? = "",
            @SerializedName("strShopName")
            var strShopName: String? = ""
        )

        data class ObjSubCategory(
            @SerializedName("fkSubCategoryId")
            var fkSubCategoryId: String? = "",
            @SerializedName("strSubCategoryName")
            var strSubCategoryName: String? = ""
        )
    }
}