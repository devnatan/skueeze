package me.devnatan.skueeze

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import kotlin.random.Random
import kotlin.random.nextUInt

const val SAMPLE_SIZE = 1000

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.MILLISECONDS)
class BitBufferBenchmark {

    private lateinit var buffer: BitBuffer
    private var randomData: UIntArray = UIntArray(SAMPLE_SIZE)
    private var randomByteLengths: IntArray = IntArray(SAMPLE_SIZE)

    @Setup
    fun setup() {
        buffer = BitBuffer()
        randomData = UIntArray(SAMPLE_SIZE) { Random.nextUInt() }
        randomByteLengths = IntArray(SAMPLE_SIZE) { Random.nextInt(1, 17) }
    }

    // ========================================================================
    // Basic Bit Operations Benchmarks
    // ========================================================================

    @Benchmark
    fun `write single bit`(): UInt {
        buffer.reset()
        buffer.writeBits(1u, 1)
        return buffer.readBits(1)
    }

    @Benchmark
    fun `write 4 bits`(): UInt {
        buffer.reset()
        buffer.writeBits(0xFu, 4)
        return buffer.readBits(4)
    }

    @Benchmark
    fun `write 8 bits`(): UInt {
        buffer.reset()
        buffer.writeBits(0xFFu, 8)
        return buffer.readBits(8)
    }

    @Benchmark
    fun `write 16 bits`(): UInt {
        buffer.reset()
        buffer.writeBits(0xFFFFu, 16)
        return buffer.readBits(16)
    }

    @Benchmark
    fun `write 32 bits`(): UInt {
        buffer.reset()
        buffer.writeBits(0xFFFFFFFFu, 32)
        return buffer.readBits(32)
    }
}