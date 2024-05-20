package dev.harsh.securesign_android.core.otp.parser

interface OtpUriParser {
    fun parseOtpUri(keyUri: String): OtpUriParserResult
}
