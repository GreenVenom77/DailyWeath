package com.dailyweath.feat_network.data

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HttpClient {
    private val executor: Executor = Executors.newCachedThreadPool()

    fun get(fullUrl: String, callback: (HttpResponse) -> Unit) {
        executor.execute {
            var connection: HttpURLConnection? = null
            try {
                connection = createConnection(fullUrl, "GET")
                val responseCode = connection.responseCode
                val responseBody = if (responseCode < 400) {
                    readStream(connection.inputStream)
                } else {
                    readStream(connection.errorStream) ?: ""
                }
                callback(HttpResponse(responseCode, responseBody ?: ""))
            } catch (e: Exception) {
                callback(HttpResponse(500, "Error: ${e.message}"))
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun readStream(inputStream: InputStream?): String? {
        return inputStream?.use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                reader.readText()
            }
        }
    }

    private fun createConnection(url: String, method: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-Type", "application/json")

        return connection
    }
}