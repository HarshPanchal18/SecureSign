package dev.harsh.securesign_android.core.otp.parser

import android.net.Uri
import dev.harsh.securesign_android.core.otp.model.OtpData
import dev.harsh.securesign_android.core.otp.model.OtpDigest
import dev.harsh.securesign_android.core.otp.model.OtpType

class OtpUriParserImpl : OtpUriParser {
    override fun parseOtpUri(keyUri: String): OtpUriParserResult {
        val uri = Uri.parse(keyUri)

        val protocol = uri.scheme?.lowercase()
        if (protocol != "otpauth")
            return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_PROTOCOL)

        val type = when (uri.host?.lowercase()) {
            "totp" -> OtpType.TOTP
            "hotp" -> OtpType.HOTP
            else -> return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_TYPE)
        }

        val label = try {
            uri.pathSegments.first() // get the decoded path string
        } catch (e: IndexOutOfBoundsException) {
            return OtpUriParserResult.Error(OtpUriParserError.ERROR_MISSING_LABEL)
        }

        val secret = uri.getQueryParameter("secret")
            ?: return OtpUriParserResult.Error(OtpUriParserError.ERROR_MISSING_SECRET)

        val issuer = uri.getQueryParameter("issuer") ?: ""

        val paramAlgorithm = uri.getQueryParameter("algorithm") ?: "SHA1"
        val algorithm = getDigest(paramAlgorithm)
            ?: return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_ALGORITHM)

        val paramDigits = uri.getQueryParameter("digits") ?: "6"
        val digits = paramDigits.toIntOrNull()
            ?: return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_DIGITS)

        val paramPeriod = uri.getQueryParameter("period") ?: "30"
        val period = try {
            if (type == OtpType.HOTP) null
            else paramPeriod.toInt()
        } catch (e: NumberFormatException) {
            return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_PERIOD)
        }

        val paramCounter = uri.getQueryParameter("counter")
        if (type == OtpType.HOTP && paramCounter == null)
            return OtpUriParserResult.Error(OtpUriParserError.ERROR_MISSING_COUNTER)

        val counter = try {
            paramCounter?.toInt()
        } catch (e: NumberFormatException) {
            return OtpUriParserResult.Error(OtpUriParserError.ERROR_INVALID_COUNTER)
        }

        val otpData = OtpData(
            label = label,
            issuer = issuer,
            secret = secret,
            digits = digits,
            algorithm = algorithm,
            type = type,
            counter = counter,
            period = period
        )

        return OtpUriParserResult.Success(otpData)
    }

    private fun getDigest(uriAlgorithm: String): OtpDigest? {
        return when (uriAlgorithm.uppercase()) {
            "SHA1" -> OtpDigest.SHA1
            "SHA256" -> OtpDigest.SHA256
            "SHA512" -> OtpDigest.SHA512
            else -> null
        }
    }
}
