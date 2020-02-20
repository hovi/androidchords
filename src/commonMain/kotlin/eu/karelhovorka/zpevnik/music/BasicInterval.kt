package eu.karelhovorka.zpevnik.music

import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

enum class BasicInterval(override val step: Int, val intervalName: String, val latin: String, val number: Int, val quality: IntervalQuality): Interval {
    PERFECT_UNISON (
    step = 0, 
    intervalName = "Perfect unison",
    latin = "unisonus",
    number = 1,
    quality = IntervalQuality.fromShortcut('P')
),MINOR_SECOND (
    step = 1, 
    intervalName = "Minor second",
    latin = "semitonus",
    number = 2,
    quality = IntervalQuality.fromShortcut('m')
),AUGMENTED_UNISON (
    step = 1, 
    intervalName = "Augmented unison",
    latin = "unisonus superflua",
    number = 1,
    quality = IntervalQuality.fromShortcut('A')
),MAJOR_SECOND (
    step = 2, 
    intervalName = "Major second",
    latin = "tonus",
    number = 2,
    quality = IntervalQuality.fromShortcut('M')
),DIMINISHED_THIRD (
    step = 2, 
    intervalName = "Diminished third",
    latin = "-",
    number = 3,
    quality = IntervalQuality.fromShortcut('d')
),MINOR_THIRD (
    step = 3, 
    intervalName = "Minor third",
    latin = "semiditonus",
    number = 3,
    quality = IntervalQuality.fromShortcut('m')
),AUGMENTED_SECOND (
    step = 3, 
    intervalName = "Augmented second",
    latin = "tonus superflua",
    number = 2,
    quality = IntervalQuality.fromShortcut('A')
),MAJOR_THIRD (
    step = 4, 
    intervalName = "Major third",
    latin = "ditonus",
    number = 3,
    quality = IntervalQuality.fromShortcut('M')
),DIMINISHED_FOURTH (
    step = 4, 
    intervalName = "Diminished fourth",
    latin = "semidiatessaron",
    number = 4,
    quality = IntervalQuality.fromShortcut('d')
),PERFECT_FOURTH (
    step = 5, 
    intervalName = "Perfect fourth",
    latin = "diatessaron",
    number = 4,
    quality = IntervalQuality.fromShortcut('P')
),AUGMENTED_THIRD (
    step = 5, 
    intervalName = "Augmented third",
    latin = "ditonus superflua",
    number = 3,
    quality = IntervalQuality.fromShortcut('A')
),DIMINISHED_FIFTH (
    step = 6, 
    intervalName = "Diminished fifth",
    latin = "semidiapente, semitritonus",
    number = 5,
    quality = IntervalQuality.fromShortcut('d')
),AUGMENTED_FOURTH (
    step = 6, 
    intervalName = "Augmented fourth",
    latin = "tritonus",
    number = 4,
    quality = IntervalQuality.fromShortcut('A')
),PERFECT_FIFTH (
    step = 7, 
    intervalName = "Perfect fifth",
    latin = "diapente",
    number = 5,
    quality = IntervalQuality.fromShortcut('P')
),DIMINISHED_SIXTH (
    step = 7, 
    intervalName = "Diminished sixth",
    latin = "semihexachordum",
    number = 6,
    quality = IntervalQuality.fromShortcut('d')
),MINOR_SIXTH (
    step = 8, 
    intervalName = "Minor sixth",
    latin = "hexachordum minus, semitonus maius cum diapente, tetratonus",
    number = 6,
    quality = IntervalQuality.fromShortcut('m')
),AUGMENTED_FIFTH (
    step = 8, 
    intervalName = "Augmented fifth",
    latin = "diapente superflua",
    number = 5,
    quality = IntervalQuality.fromShortcut('A')
),MAJOR_SIXTH (
    step = 9, 
    intervalName = "Major sixth",
    latin = "hexachordum maius, tonus cum diapente",
    number = 6,
    quality = IntervalQuality.fromShortcut('M')
),DIMINISHED_SEVENTH (
    step = 9, 
    intervalName = "Diminished seventh",
    latin = "semiheptachordum",
    number = 7,
    quality = IntervalQuality.fromShortcut('d')
),MINOR_SEVENTH (
    step = 10, 
    intervalName = "Minor seventh",
    latin = "heptachordum minus, semiditonus cum diapente, pentatonus",
    number = 7,
    quality = IntervalQuality.fromShortcut('m')
),AUGMENTED_SIXTH (
    step = 10, 
    intervalName = "Augmented sixth",
    latin = "hexachordum superflua",
    number = 6,
    quality = IntervalQuality.fromShortcut('A')
),MAJOR_SEVENTH (
    step = 11, 
    intervalName = "Major seventh",
    latin = "heptachordum maius, ditonus cum diapente",
    number = 7,
    quality = IntervalQuality.fromShortcut('M')
),DIMINISHED_OCTAVE (
    step = 11, 
    intervalName = "Diminished octave",
    latin = "semidiapason",
    number = 8,
    quality = IntervalQuality.fromShortcut('d')
),PERFECT_OCTAVE (
    step = 12, 
    intervalName = "Perfect octave",
    latin = "diapason",
    number = 8,
    quality = IntervalQuality.fromShortcut('P')
),AUGMENTED_SEVENTH (
    step = 12, 
    intervalName = "Augmented seventh",
    latin = "heptachordum superflua",
    number = 7,
    quality = IntervalQuality.fromShortcut('A')
);

    companion object {
        val P1: Interval
    @JvmName("getP180")
    get() = PERFECT_UNISON
val m2: Interval
    @JvmName("getm2109")
    get() = MINOR_SECOND
val A1: Interval
    @JvmName("getA165")
    get() = AUGMENTED_UNISON
val M2: Interval
    @JvmName("getM277")
    get() = MAJOR_SECOND
val d3: Interval
    @JvmName("getd3100")
    get() = DIMINISHED_THIRD
val m3: Interval
    @JvmName("getm3109")
    get() = MINOR_THIRD
val A2: Interval
    @JvmName("getA265")
    get() = AUGMENTED_SECOND
val M3: Interval
    @JvmName("getM377")
    get() = MAJOR_THIRD
val d4: Interval
    @JvmName("getd4100")
    get() = DIMINISHED_FOURTH
val P4: Interval
    @JvmName("getP480")
    get() = PERFECT_FOURTH
val A3: Interval
    @JvmName("getA365")
    get() = AUGMENTED_THIRD
val d5: Interval
    @JvmName("getd5100")
    get() = DIMINISHED_FIFTH
val A4: Interval
    @JvmName("getA465")
    get() = AUGMENTED_FOURTH
val P5: Interval
    @JvmName("getP580")
    get() = PERFECT_FIFTH
val d6: Interval
    @JvmName("getd6100")
    get() = DIMINISHED_SIXTH
val m6: Interval
    @JvmName("getm6109")
    get() = MINOR_SIXTH
val A5: Interval
    @JvmName("getA565")
    get() = AUGMENTED_FIFTH
val M6: Interval
    @JvmName("getM677")
    get() = MAJOR_SIXTH
val d7: Interval
    @JvmName("getd7100")
    get() = DIMINISHED_SEVENTH
val m7: Interval
    @JvmName("getm7109")
    get() = MINOR_SEVENTH
val A6: Interval
    @JvmName("getA665")
    get() = AUGMENTED_SIXTH
val M7: Interval
    @JvmName("getM777")
    get() = MAJOR_SEVENTH
val d8: Interval
    @JvmName("getd8100")
    get() = DIMINISHED_OCTAVE
val P8: Interval
    @JvmName("getP880")
    get() = PERFECT_OCTAVE
val A7: Interval
    @JvmName("getA765")
    get() = AUGMENTED_SEVENTH
val M9: Interval
    @JvmName("getM2M950")
    get() = M2
val m9: Interval
    @JvmName("getm2m950")
    get() = m2
val P11: Interval
    @JvmName("getP4P1152")
    get() = P4
val M13: Interval
    @JvmName("getM6M1354")
    get() = M6
val m13: Interval
    @JvmName("getm6m1354")
    get() = m6
        
        @JvmStatic
        fun of(step: Int): Interval {
            return values()[(step % 12 + 12) % 12]
        }
    }
}