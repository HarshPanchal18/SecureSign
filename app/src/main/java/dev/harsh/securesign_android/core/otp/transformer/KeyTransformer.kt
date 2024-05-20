package dev.harsh.securesign_android.core.otp.transformer

interface KeyTransformer {

    fun transformToBytes(key: String): ByteArray

}
