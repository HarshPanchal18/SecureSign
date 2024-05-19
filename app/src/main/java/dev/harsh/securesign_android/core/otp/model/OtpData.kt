package dev.harsh.securesign_android.core.otp.model

data class OtpData(
    val label: String,
    val issuer: String,
    val secret: String, // secret key used to generate OTP.
    val digits: Int, // the number of digits in the OTP.
    val algorithm: OtpDigest,
    val type: OtpType,
    val counter: Int?, // counter value used to generate the OTP.
    val period: Int? //  the period in seconds for which the OTP is valid.
)
