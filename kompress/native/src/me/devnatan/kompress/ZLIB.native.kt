package me.devnatan.kompress

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import platform.zlib.Z_DEFAULT_COMPRESSION
import platform.zlib.Z_DEFAULT_STRATEGY
import platform.zlib.Z_DEFLATED
import platform.zlib.Z_FINISH
import platform.zlib.Z_OK
import platform.zlib.compressBound
import platform.zlib.deflate
import platform.zlib.deflateEnd
import platform.zlib.deflateInit2
import platform.zlib.inflate
import platform.zlib.inflateEnd
import platform.zlib.inflateInit2
import platform.zlib.z_stream

private const val BUF_SIZE = 4096

// Z_OK = 0
// Z_STREAM_END = 1
// Z_FINISH = 4
// Z_DEFLATED = 8

@OptIn(ExperimentalForeignApi::class)
internal actual fun zlibInflate(input: ByteArray): ByteArray =
    memScoped {
        val stream = alloc<z_stream>()
        stream.avail_in = input.size.toUInt()
        stream.next_in = input.refTo(0).getPointer(memScope).reinterpret()

        inflateInit2(stream.ptr, 15 + 16)

        var buf = ByteArray(BUF_SIZE)
        var result: Int
        do {
            val size = buf.size.toULong()
            if (stream.total_out >= size) {
                buf = buf.copyOf(BUF_SIZE)
            }

            stream.avail_out = (size - stream.total_out).toUInt()
            stream.next_out = buf.refTo(stream.total_out.toInt()).getPointer(memScope).reinterpret()

            result = inflate(stream.ptr, Z_FINISH)
        } while (result == Z_OK)
        inflateEnd(stream.ptr)

        return buf.copyOf(stream.total_out.toInt())
    }

@OptIn(ExperimentalForeignApi::class)
internal actual fun zlibDeflate(input: ByteArray): ByteArray =
    memScoped {
        val compressedBuffer = ByteArray(compressBound(input.size.toUInt()).toInt())

        val stream = alloc<z_stream>()
        stream.avail_in = input.size.toUInt()
        stream.next_in = input.refTo(0).getPointer(memScope).reinterpret()
        stream.next_out = compressedBuffer.refTo(0).getPointer(memScope).reinterpret()
        stream.avail_out = compressedBuffer.size.toUInt()

        deflateInit2(stream.ptr, Z_DEFAULT_COMPRESSION, Z_DEFLATED, 15 + 16, 8, Z_DEFAULT_STRATEGY)
        deflate(stream.ptr, Z_FINISH)
        deflateEnd(stream.ptr)

        val compressedSize = compressedBuffer.size - stream.avail_out.toInt()
        return compressedBuffer.copyOf(compressedSize)
    }
