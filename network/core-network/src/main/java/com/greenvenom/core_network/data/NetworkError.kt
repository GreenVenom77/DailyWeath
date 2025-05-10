package com.greenvenom.core_network.data

import com.greenvenom.core_network.domain.Error

data class NetworkError(
    val errorType: ErrorType
): Error
