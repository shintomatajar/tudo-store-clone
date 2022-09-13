package com.tudomart.store.model


import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModelTimeSlotResponse(
    @SerializedName("data")
    val `data`: List<Data?>? = listOf(),
    @SerializedName("message")
    val message: String? = "", // Success
    @SerializedName("success")
    val success: Boolean? = false // true
) : Serializable {
    data class Data(
        @SerializedName("dateSlot")
        val dateSlot: String? = "", // 2021-07-09T00:00:00.000Z
        @SerializedName("fkStoreId")
        val fkStoreId: String? = "", // 60d8b4824fb4db27b613fd65
        @SerializedName("intCapacity")
        val intCapacity: Int? = 0, // 30
        @SerializedName("intDeliveryCharge")
        val intDeliveryCharge: Double? = 0.0, // 100.01
        @SerializedName("intOrdersCount")
        val intOrdersCount: Int? = 0, // 0
        @SerializedName("pkTimeSlotId")
        val pkTimeSlotId: String? = "", // 60e73013b718bf12008675ca
        @SerializedName("strDeliveryType")
        val strDeliveryType: String? = "", // NORMAL
        @SerializedName("strDisplayName")
        val strDisplayName: String? = "", // TEST
        @SerializedName("strFromTime")
        val strFromTime: String? = "", // 2021-07-08T20:00:00.000Z
        @SerializedName("strToTime")
        val strToTime: String? = "" // 2021-07-09T13:00:00.000Z
    ) : Serializable {
        data class ObjShop(
            @SerializedName("fkShopId")
            val fkShopId: String? = "", // 60d8b4824fb4db27b613fd65
            @SerializedName("strImageUrl")
            val strImageUrl: String? = "", // https://matajarbucket.s3.me-south-1.amazonaws.com/test/Grand.jpg
            @SerializedName("strPlace")
            val strPlace: String? = "", // Stadium
            @SerializedName("strShopName")
            val strShopName: String? = "" // Grand Supermarket
        ) : Serializable
    }
}