package dev.harsh.securesign_android.core.otp.generator

import dev.harsh.securesign_android.core.otp.model.OtpDigest

interface OtpGenerator {

    fun generateTotp(
        secret: ByteArray,
        interval: Long,
        seconds: Long,
        digits: Int = 6,
        digest: OtpDigest = OtpDigest.SHA1,
    ): String

    fun generateHotp(
        secret: ByteArray,
        counter: Long,
        digits: Int = 6,
        digest: OtpDigest = OtpDigest.SHA1,
    ): String

}
