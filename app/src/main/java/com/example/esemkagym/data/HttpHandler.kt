package com.example.esemkagym.data

import android.util.Log
import com.example.esemkagym.data.model.Http
import com.example.esemkagym.utils.Helper
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler {
    fun request(
        endpoint: String,
        method: String? = "GET",
        token: String? = null,
        rBody: String? = null
    ): Http {
        return try {
            val conn = URL(Helper.url + endpoint).openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.setRequestProperty("Content-Type", "application/json")

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }

            if (rBody != null) {
                conn.doOutput = true
                conn.outputStream.use { it.write(rBody.toByteArray()) }
            }

            val code = conn.responseCode
            val body = try {
                conn.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                e.printStackTrace()
                conn.errorStream.bufferedReader().use { it.readText() }
            }

            Log.d("debugData", "code: $code body: $body")
            Http(code, body)
        } catch (e: Exception) {
            e.printStackTrace()
            Http(500, e.message ?: "error")
        }
    }
}