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
}