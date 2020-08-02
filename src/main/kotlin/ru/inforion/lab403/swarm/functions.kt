package ru.inforion.lab403.swarm

import ru.inforion.lab403.swarm.implementations.MPI
import ru.inforion.lab403.swarm.implementations.Threads
import java.lang.IllegalArgumentException

fun <T> Collection<T>.parallelize(swarm: Swarm) = swarm.parallelize(this)

fun <T> Array<T>.parallelize(swarm: Swarm) = swarm.parallelize(this.asList())

fun threadsSwarm(size: Int, compress: Boolean = false, code: (Swarm) -> Unit) {
    Swarm(Threads(size, compress), code)
}

fun mpiSwarm(vararg args: String, compress: Boolean = false, code: (Swarm) -> Unit) {
    Swarm(MPI(*args, compress = compress), code)
}

fun swarm(name: String, compress: Boolean, vararg args: Any, code: (Swarm) -> Unit) = when(name) {
    "THREAD" -> threadsSwarm(args[0] as Int, compress, code)
    "MPI" -> mpiSwarm(*args as Array<out String>, compress = compress, code = code)
    else -> throw IllegalArgumentException("Unknown realm '$name' for Swarm!")
}

fun <T> Collection<T>.separate(count: Int): Collection<Collection<T>> {
    require(count > 0) { "Collection size must be > 0" }
    val chunkSize = size / count + 1
    return windowed(chunkSize, chunkSize, true)
}