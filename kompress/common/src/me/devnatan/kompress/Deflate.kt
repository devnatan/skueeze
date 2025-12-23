package me.devnatan.kompress

internal class DeflateAlgorithm : CompressionAlgorithm {

    override fun compress(input: ByteArray, level: CompressionLevel): ByteArray {
        val compressor = DeflateCompressor(level)
        val result = compressor.process(input, 0, input.size)
        return result + compressor.finish()
    }

    override fun decompress(input: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}

internal class DeflateCompressor(
    val compressionLevel: CompressionLevel
) {

    var bytesRead: Long = 0L
    var bytesWritten: Long = 0L

    var buf = mutableListOf<Byte>()
    var finished = false

    fun process(input: ByteArray, offset: Int, length: Int): ByteArray {
        require(!finished)
        bytesRead += length

        val compressed = when(compressionLevel) {
            CompressionLevel.NO_COMPRESSION -> uncompressed(input, offset, length)
            else -> compressWithLZ77(input, offset, length)
        }

        buf.addAll(compressed.toList())
        return ByteArray(0) // Mutable buffer
    }

    fun finish(): ByteArray {
        finished = true

        val result = buf.toByteArray()
        bytesWritten = result.size.toLong()
        return result
    }

    private fun uncompressed(input: ByteArray, offset: Int, length: Int): ByteArray {
        // TODO Format correctly based
        return input.copyOfRange(offset, offset + length)
    }

    private fun compressWithLZ77(input: ByteArray, offset: Int, length: Int): ByteArray {
        // TODO LZ77 sliding window + Huffman
        return input.copyOfRange(offset, offset + length)
    }
}