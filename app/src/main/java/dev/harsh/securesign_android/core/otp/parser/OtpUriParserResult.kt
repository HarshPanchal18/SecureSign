package dev.harsh.securesign_android.core.otp.parser

import dev.harsh.securesign_android.core.otp.model.OtpData

sealed interface OtpUriParserResult {
    data class Success(val data: OtpData) : OtpUriParserResult
    data class Error(val error: OtpUriParserError) : OtpUriParserResult
}
