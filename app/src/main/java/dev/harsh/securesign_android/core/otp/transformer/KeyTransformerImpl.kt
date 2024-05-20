package dev.harsh.securesign_android.core.otp.transformer

import org.apache.commons.codec.binary.Base32

class KeyTransformerImpl : KeyTransformer {
    val base32 = Base32()
    override fun transformToBytes(key: String): ByteArray {
        val trimmedKey = key.trim().replace("-", "").replace(" ", "")
        return base32.decode(trimmedKey)
    }
}
