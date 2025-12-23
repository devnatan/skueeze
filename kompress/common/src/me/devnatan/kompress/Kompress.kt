package me.devnatan.kompress

import kotlin.math.abs

public object Kompress {
}

// TODO Incremental compression and decompression
public interface CompressionAlgorithm {

    public fun compress(
        input: ByteArray,
        level: CompressionLevel = CompressionLevel.DEFAULT
    ): ByteArray

    public fun decompress(input: ByteArray): ByteArray
}

public enum class CompressionLevel(private val value: Int) {

    NO_COMPRESSION(0),
    FASTEST(1),
    FAST(3),
    DEFAULT(6),
    BEST(9);

    public companion object {

        public fun fromValue(value: Int): CompressionLevel = entries.minByOrNull { level ->
            abs(level.value - value)
        } ?: DEFAULT
    }
}