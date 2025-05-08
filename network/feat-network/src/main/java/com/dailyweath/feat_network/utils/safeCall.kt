package com.dailyweath.feat_network.utils

import com.dailyweath.feat_network.data.HttpResponse
import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import java.io.IOException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

inline fun <reified T> safeCall (
    execute: () -> HttpResponse
): NetworkResult<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnknownHostException) {
        return NetworkResult.Error(NetworkError(ErrorType.NO_INTERNET))
    } catch (e: UnresolvedAddressException) {
        return NetworkResult.Error(NetworkError(ErrorType.NO_INTERNET))
    } catch (e: IOException) {
        return NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR))
    } catch (e: Exception) {
        return NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR))
    }
    return responseToResult(response)
}