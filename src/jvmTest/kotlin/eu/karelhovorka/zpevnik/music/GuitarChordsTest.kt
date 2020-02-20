package eu.karelhovorka.zpevnik.music

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import eu.karelhovorka.zpevnik.util.Tone
import eu.karelhovorka.zpevnik.util.toToneIndex
import org.junit.Test
import java.io.File

class GuitarChordsTest {
    @Test
    fun testParseJson() {
        val jsonString = File("src/commonMain/resources/guitar.json").readText()
        val parser = JsonParser()
        val jsonTree = parser.parse(jsonString).asJsonObject
        val chords = jsonTree["chords"].asJsonObject
        val keys = jsonTree["keys"].asJsonArray.map { it.asString.replace("#", "sharp") }
        val suffixes = jsonTree["suffixes"].asJsonArray.map { it.asString }

        println(getNotFoundChords(suffixes))

        keys.forEach { key ->
            //println(key)
            val cChords = chords[key].asJsonArray.map { it.asJsonObject }
            checkChord(cChords)
        }

        //print(x)
    }

    fun getNotFoundChords(suffixes: List<String>): List<String> {
        return suffixes.filter {
            ChordType.fromString(it) == ChordType.UNKNOWN_CHORD
        }

    }

    fun checkChord(list: List<JsonObject>) {
        val items = mutableSetOf<String>()
        val x = list.map {
            val suffix = it["suffix"].asString
            val chordType = ChordType.fromString(suffix)
            val tone: Tone = Tone.fromString(it["key"].asString)!!
            val chord = Chord(tone, chordType)
            val positions = it["positions"].asJsonArray.map { it.asJsonObject }.map {
                val c: Int = it["baseFret"].asInt
                val capo = if (c == 1) {
                    0
                } else {
                    c - 1
                }
                capo to it["frets"].asJsonArray.map { it.asInt }
            }.toMap()
            if (chordType == ChordType.UNKNOWN_CHORD) {

            } else {

                positions.map { position ->
                    checkPosition(chord, position.component2(), position.component1())
                    //return
                }
            }

        }
    }

    fun checkPosition(chord: Chord, positions: List<Int>, capo: Int = 0) {

        val positionsToTones = positionsToTones(capo, positions)
        val positionsToSteps = positionsToTones.map { it.index }
        val missingIntervals = chord.type.intervals.filter { interval ->
            val result = ((interval.step + chord.tone.index).toToneIndex() !in positionsToSteps)
            result
        }.filter { it != BasicInterval.PERFECT_FIFTH }

        val intervalSteps = chord.type.intervals.map { (it.step + chord.tone.index).toToneIndex() }
        val extraTones = positionsToTones.filter { pos ->
            pos.index !in intervalSteps
        }

        if (missingIntervals.isNotEmpty() || extraTones.isNotEmpty()) {
            //println(positionsToSteps)
            println("$chord $positions ($capo) ${positionsToTones.map { it.name }}:")
            if (missingIntervals.isNotEmpty()) {
                println("missing intervals ${missingIntervals} (${missingIntervals.map { chord.tone.transpose(it) }})")
            }
            if (extraTones.isNotEmpty()) {
                println("extra tones ${extraTones} ")
            }

        }
    }

    private fun positionsToTones(base: Int, positions: List<Int>): List<Tone> {
        val tonesOnGuitar = listOf(
                Tone.E,
                Tone.A,
                Tone.D,
                Tone.G,
                Tone.H,
                Tone.E
        )
        return positions.mapIndexed { index, p ->
            //println("${tonesOnGuitar[index].index} $base $p")
            if (p == -1) {
                -1
            } else {
                if (p == 0) {
                    tonesOnGuitar[index].index
                } else {
                    tonesOnGuitar[index].index + base + p
                }
            }
        }.filter {
            it != -1
        }.map {
            Tone.fromIndex(it)
        }
    }
}