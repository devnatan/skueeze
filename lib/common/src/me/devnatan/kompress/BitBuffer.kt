package me.devnatan.kompress

/** Internally manages a 32-bit buffer and tracks how many bits are currently stored. */
internal class BitBuffer {
    private var buffer: UInt = 0u
    private var bitsInBuffer: Int = 0

    fun writeBits(value: UInt, numBits: Int) {
        require(numBits in 1..32) { "numBits must be between 1 and 32, got $numBits" }

        val masked = if (numBits == 32) {
            value
        } else {
            value and ((1u shl numBits) - 1u)
        }

        if (bitsInBuffer + numBits <= 32) {
            buffer = buffer or (masked shl bitsInBuffer)
            bitsInBuffer += numBits
        } else {
            // would overflow 32-bit buffer (this shouldn't happen?)
            // but we handle it by only taking what fits
            val bitsToWrite = 32 - bitsInBuffer
            val partialMask = (1u shl bitsToWrite) - 1u
            buffer = buffer or ((masked and partialMask) shl bitsInBuffer)
            bitsInBuffer = 32
        }
    }

    fun readBits(numBits: Int): UInt {
        require(numBits in 1..32) { "numBits must be between 1 and 32, got $numBits" }
        require(bitsInBuffer >= numBits) { "Not enough bits in buffer: need $numBits, have $bitsInBuffer" }

        val value = if (numBits == 32)
            buffer
        else
            buffer and ((1u shl numBits) - 1u)

        buffer = if (numBits == 32)
            0u
        else
            buffer shr numBits

        bitsInBuffer -= numBits

        return value
    }

    fun hasBits(numBits: Int): Boolean = bitsInBuffer >= numBits

    fun alignToByte() {
        val bitsToSkip = bitsInBuffer % 8
        if (bitsToSkip <= 0) return

        buffer = buffer shr bitsToSkip
        bitsInBuffer -= bitsToSkip
    }

    fun reset() {
        buffer = 0u
        bitsInBuffer = 0
    }

    fun availableBytes(): Int = bitsInBuffer / 8

    fun readByte(): UByte {
        require(bitsInBuffer >= 8) { "Not enough bits for a byte. Given: $bitsInBuffer" }
        return readBits(8).toUByte()
    }

    fun writeByte(byte: UByte) {
        writeBits(byte.toUInt(), 8)
    }
}