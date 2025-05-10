package com.greenvenom.core_network.data

import androidx.annotation.StringRes
import com.greenvenom.core_network.R

enum class ErrorType(
    @StringRes val resId: Int
) {
    // HTTP Status Codes
    BAD_REQUEST(R.string.error_bad_request),              // 400
    UNAUTHORIZED(R.string.error_unauthorized),             // 401
    FORBIDDEN(R.string.error_forbidden),                // 403
    NOT_FOUND(R.string.error_not_found),                // 404
    REQUEST_TIMEOUT(R.string.error_request_timeout),          // 408
    CONFLICT(R.string.error_conflict),                 // 409
    SERIALIZATION_ERROR(R.string.error_serialization),      // 422
    TOO_MANY_REQUESTS(R.string.error_too_many_requests),        // 429
    SERVER_ERROR(R.string.error_server),             // 500
    SERVICE_UNAVAILABLE(R.string.error_service_unavailable),      // 503

    // Custom Errors
    NO_INTERNET(R.string.error_no_internet),              // Custom
    UNKNOWN_ERROR(R.string.error_unknown)             // Custom
}