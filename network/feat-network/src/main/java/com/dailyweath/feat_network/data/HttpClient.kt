package com.dailyweath.feat_network.data

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HttpClient {
    fun get(fullUrl: String, connectionTimeout: Long = 5000, readTimeout: Long = 5000): HttpResponse {
        var connection: HttpURLConnection? = null
        try {
            connection = createConnection(fullUrl, "GET", connectionTimeout, readTimeout)
            val responseCode = connection.responseCode
            val responseBody = if (responseCode < 400) {
                readStream(connection.inputStream)
            } else {
                readStream(connection.errorStream) ?: ""
            }
            return HttpResponse(responseCode, responseBody ?: "")
        } finally {
            connection?.disconnect()
        }
    }

    private fun readStream(inputStream: InputStream?): String? {
        return inputStream?.use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                reader.readText()
            }
        }
    }

    private fun createConnection(
        url: String,
        method: String,
        connectionTimeout: Long,
        readTimeout: Long
    ): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        return connection
    }
}