package com.tudomart.store.api

import com.tudomart.store.helpers.network.ApiUrl
import com.tudomart.store.ui.activities.ordersubstitute.ModelSubCategoryProductsResponse
import com.tudomart.store.ui.activities.ordersubstitute.ModelSubstituteListResponse
import com.tudomart.store.utils.WebService
import android.app.Activity
import com.google.gson.Gson
import org.json.JSONObject

class ProductsAPI(activity: Activity?) : com.tudomart.store.utils.WebService(activity) {
    fun getSubstitutedProducts(
        orderId: String,
        productId: String,
        onSuccess: (List<ModelSubCategoryProductsResponse.Data?>) -> Unit, _onError: (String) -> Unit
    ) {

        val req = JSONObject().apply {
            put("strOrderId", orderId)
            put("arr_OutOfStockProductId", productId)
        }

        val path = com.tudomart.store.helpers.network.ApiUrl.SUBSTITUTED_PRODUCTS_LIST_URL
        post(path, req, object : OnApiCallback {
            override fun onSuccess(reponse: JSONObject?) {
                val resp =
                    Gson().fromJson(reponse?.toString(), ModelSubstituteListResponse::class.java)
                if (resp.success == true) {
                    resp.data?.let {
                        onSuccess(it)
                        return
                    }
                }
                onError(resp.message)
            }

            override fun onError(error: String?) {
                _onError(error ?: "")
            }

        })

    }

    fun clearSubstitution(
        orderId: String,
        productId: String,
        onSuccess: (String) -> Unit, _onError: (String) -> Unit
    ) {

        val req = JSONObject().apply {
            put("strOrderId", orderId)
            put("arr_OutOfStockProductId", productId)
        }

        val path = com.tudomart.store.helpers.network.ApiUrl.CLEAR_SUBSTITUTION
        post(path, req, object : OnApiCallback {
            override fun onSuccess(reponse: JSONObject?) {
                val resp = reponse?.getBoolean("success") ?: false

                if (resp) {
                    onSuccess("")
                }
                onError(reponse?.getString("message"))
            }

            override fun onError(error: String?) {
                _onError(error ?: "")
            }

        })

    }
}