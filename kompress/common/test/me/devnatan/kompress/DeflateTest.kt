package me.devnatan.kompress

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DeflateTest {

    private val deflate = DeflateAlgorithm()

    @Test
    fun `compress empty`() {
        val empty = ByteArray(0)
        val compressed = deflate.compress(empty)

        assertNotNull(compressed)
        assertTrue(compressed.isEmpty())
    }
}