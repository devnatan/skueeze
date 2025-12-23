package me.devnatan.kompress

import kotlin.test.Test
import kotlin.test.assertEquals

class BitBufferTest {

    @Test
    fun `write and read zero`() {
        val buffer = BitBuffer()
        buffer.writeBits(0u, 1)
        assertEquals(
            expected = 0u,
            actual = buffer.readBits(1)
        )
    }

    @Test
    fun `write and read single`() {
        val buffer = BitBuffer()
        buffer.writeBits(1u, 1)
        assertEquals(
            expected = 1u,
            actual = buffer.readBits(1)
        )
    }

    @Test
    fun `write and read full`() {
        val buffer = BitBuffer()
        buffer.writeBits(0xFFu, 8)
        assertEquals(
            expected = 0xFFu,
            actual = buffer.readBits(8)
        )
    }

    @Test
    fun `write and read 16`() {
        val buffer = BitBuffer()
        buffer.writeBits(0xABCDu, 16)
        assertEquals(
            expected = 0xABCDu,
            actual = buffer.readBits(16)
        )
    }

    @Test
    fun `write and read 32`() {
        val buffer = BitBuffer()
        buffer.writeBits(0x12345678u, 32)
        assertEquals(
            expected = 0x12345678u,
            actual = buffer.readBits(32)
        )
    }

    @Test
    fun `write and read 32 max`() {
        val buffer = BitBuffer()
        buffer.writeBits(0xFFFFFFFFu, 32)
        assertEquals(
            expected = 0xFFFFFFFFu,
            actual = buffer.readBits(32)
        )
    }
}