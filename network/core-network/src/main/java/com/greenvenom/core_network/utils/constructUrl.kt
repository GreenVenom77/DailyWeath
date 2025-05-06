package com.greenvenom.core_network.utils

import com.greenvenom.core_network.BuildConfig
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun constructUrl(
    basePath: String,
    pathParams: List<String> = emptyList(),
    queryParams: Map<String, String> = emptyMap()
): String {
    val fullUrl = when {
        basePath.contains(BuildConfig.BASE_URL) -> basePath
        basePath.startsWith("/") -> BuildConfig.BASE_URL + basePath.drop(1)
        else -> BuildConfig.BASE_URL + basePath
    }

    val urlWithPath = if (pathParams.isNotEmpty()) {
        "$fullUrl/${pathParams.joinToString("/") { URLEncoder.encode(it, "UTF-8") }}"
    } else {
        fullUrl
    }

    val queryString = buildQueryString(queryParams)

    return if (queryString.isNotEmpty()) {
        val separator = if (urlWithPath.contains("?")) "&" else "?"
        "$urlWithPath$separator$queryString"
    } else {
        urlWithPath
    }
}

private fun buildQueryString(params: Map<String, String>): String {
    val queryParams = mutableMapOf<String, String>().apply {
        putAll(params)

        if (BuildConfig.API_KEY.isNotBlank()) {
            put("key", BuildConfig.API_KEY)
        }
    }

    return queryParams.map { (key, value) ->
        "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
    }.joinToString("&")
}