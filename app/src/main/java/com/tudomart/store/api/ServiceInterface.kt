package com.matajarbynesto.customer.data.api.volley

import org.json.JSONObject

interface ServiceInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit, failureHandler:(error: String?) -> Unit)
    fun get(path: String, completionHandler: (response: JSONObject?) -> Unit, failureHandler:(error: String?) -> Unit)
}
