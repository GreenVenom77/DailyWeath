package com.dailyweath.feat_network.utils

import com.dailyweath.feat_network.data.HttpResponse
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.utils.toNetworkError

inline fun <reified T> responseToResult(
    response: HttpResponse
): NetworkResult<T, NetworkError> {
    return when (response.status) {
        in 200..299 -> {
            NetworkResult.Success(response.body<T>())
        }
        else -> NetworkResult.Error(toNetworkError(response.status))
    }
}