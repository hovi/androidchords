package eu.karelhovorka.zpevnik.music

import kotlin.jvm.JvmStatic


interface Interval {
    val step: Int


    companion object {

        @JvmStatic
        fun of(step: Int): Interval {
            val normalizedStep = (step % 12 + 12) % 12
            return BasicInterval.values().first { it.step == normalizedStep }
        }
    }
}
