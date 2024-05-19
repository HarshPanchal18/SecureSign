package dev.harsh.securesign_android.core.otp.generator

import dev.harsh.securesign_android.core.otp.model.OtpDigest
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.floor
import kotlin.math.pow

class OtpGeneratorImpl : OtpGenerator {
    override fun generateTotp(
        secret: ByteArray,
        interval: Long,
        seconds: Long,
        digits: Int,
        digest: OtpDigest,
    ): String {
        val counter = floor((seconds / interval).toDouble()).toLong()
        return generateHotp(secret, counter, digits, digest)
    }

    override fun generateHotp(
        secret: ByteArray,
        counter: Long,
        digits: Int,
        digest: OtpDigest,
    ): String {
        val hash: ByteArray = Mac.getInstance(digest.name).let { mac ->
            val byteCounter = ByteBuffer.allocate(8) // Allocates a new byte buffer.
                .putLong(counter)
                .array()

            mac.init(/* key = */ SecretKeySpec(/* key = */ secret, /* algorithm = */ "RAW"))
            mac.doFinal(byteCounter)
        }

        val offset: Int = hash[hash.lastIndex].toInt() and 0xF

        val code: Int = ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset + 1].toInt() and 0xff) shl 16) or
                ((hash[offset + 2].toInt() and 0xff) shl 8) or
                ((hash[offset + 3].toInt() and 0xff))

        val paddedCode = (code % 10.0.pow(digits)).toInt()

        return StringBuilder(paddedCode.toString()).apply {
            while (length < digits) {
                insert(0, "0")
            }
        }.toString()
    }
}
