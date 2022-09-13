package com.matajarbynesto.customer.data.api.volley

import com.tudomart.store.utils.MatajarApp
import com.tudomart.store.utils.Utility
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class ServiceVolley : ServiceInterface {

    val TAG = ServiceVolley::class.java.simpleName

    override fun post(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit,
        failureHandler: (error: String?) -> Unit
    ) {
        serviceCall(path, params, completionHandler, failureHandler)
    }

    override fun get(
        path: String,
        completionHandler: (response: JSONObject?) -> Unit,
        failureHandler: (error: String?) -> Unit
    ) {
        serviceCall(path, null, completionHandler, failureHandler)
    }

    private fun serviceCall(
        path: String,
        params: JSONObject?,
        completionHandler: (response: JSONObject?) -> Unit,
        failureHandler: (error: String?) -> Unit
    ) {
        params?.toString()?.let { Log.d("REQ", it) }

        if (!Utility.isNetworkAvailable()) {
            Utility.hideLoaderDialog()
            failureHandler("No Internet Connection")
            return
        }

        Log.d("Url", path)
        val jsonRequest =
            object : JsonObjectRequest(path, params, Response.Listener<JSONObject> { response ->
                Log.d("RESPO", response.toString())
                validateResponse(response, path, params, completionHandler, failureHandler)
            },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    if (error?.networkResponse != null) {
                        Log.d("Response Error:********", path + "\n\n" + error.networkResponse)
                        if (error.networkResponse.statusCode == 500) {
                            failureHandler("An error occurred.")
                        } else {
                            if (error.message == null) {
                                failureHandler("An error occurred.")
                            } else {
                                failureHandler(error.message)
                            }
                        }
                    } else {
                        Log.d("Response Error:********", path + "\n\n" + error)
                        failureHandler("An error occurred.")
                    }
                }) {
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): Map<String, String> {
//                    return getCustomHeaders()
//                }
            }
        jsonRequest.retryPolicy = DefaultRetryPolicy(
            60000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        MatajarApp.get().addToRequestQueue(jsonRequest, TAG)
    }

    private fun validateResponse(
        response: JSONObject,
        path: String,
        params: JSONObject?,
        completionHandler: (response: JSONObject?) -> Unit,
        failureHandler: (error: String?) -> Unit
    ) {
        Utility.hideLoaderDialog()
        try {
            val success =
                if (path.contains("googleapis")) { // for geocoding
                    response.getString("status") == "OK"
                } else {
                    response.getBoolean(
                        "success"
                    )
                }
            if (success) {
                completionHandler(response)
            } else {
                failureHandler(
                    if (path.contains("googleapis")) response.getString("error_message") else response.getString(
                        "message"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            failureHandler("An error occurred.")
        }
    }

    private fun getCustomHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        return headers
    }
}