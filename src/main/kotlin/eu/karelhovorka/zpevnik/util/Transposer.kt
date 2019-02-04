package eu.karelhovorka.zpevnik.util

import mock.JvmField
import mock.JvmOverloads
import mock.JvmStatic
import mock.toPattern

object Transposer {

    val MINIMUM_SEQUENCE_COUNT = 2

    val SINGLE_TONE = "[A-Habcdefgh]\\#?b?" // b

    val TONE_ADDITIONS = "7?(?:sus|maj|mimaj|add|aug|dim|min|mi|m|b|\\+)?[1-9]{0,2}(?:\\+)?"

    // private static final String SINGLE_CHORD = "(" + SINGLE_TONE + ")" +
    // TONE_ADDITIONS;

    //public static final String FULL_CHORD = "(" + SINGLE_TONE + ")" + TONE_ADDITIONS + "(?:(\\/)(" + SINGLE_TONE + ")" + TONE_ADDITIONS + ")?";

    val FULL_CHORD = "($SINGLE_TONE)($TONE_ADDITIONS)(?:(\\/)($SINGLE_TONE)?(($TONE_ADDITIONS)))?"

    // private static final String FULL_CHORD =
    // "([A-H]\\#?)(mi?|sus|maj)?[0-9]?(\\/([A-H]\\#?))?";

    // public static final String CHORD_REGEX = CHORD_1 + CHORD_2 + CHORD_3 +
    // CHORD_4 + CHORD_5 + CHORD_6;

    @JvmField
    val CHORD_REGEX = "\\[($FULL_CHORD)\\]"

    private val MULTI_CHORD = ("\\[(\\s*$FULL_CHORD,?\\s*){2,}\\]").toPattern()

    fun getChordRegex(tone1: String, tone2: String): String {
        return "\\[($tone1)($TONE_ADDITIONS)((\\/)($tone2)(($TONE_ADDITIONS)))\\]"
    }

    fun getChordRegex(tone: String): String {
        return "\\[($tone)($TONE_ADDITIONS)\\]"
    }

    fun merge3(text: String): String {
        val m = MULTI_CHORD.matcher(text)
        var result = text
        if (m.find()) {
            result = text.substring(0, m.start()) + split(m.group().substring(1, m.group().length - 1)) + text.substring(m.end())
            return if (result == text) {
                result
            } else merge(result)
        }
        return result
    }

    fun merge(text: String): String {
        val lines = text.split("\n".toRegex())
        val result = StringBuilder()
        for (line in lines) {
            result.append(mergeLine(line) + "\n")
        }
        return result.toString().trimEnd()
    }

    fun mergeLine(text: String): String {
        val m = MULTI_CHORD.matcher(text)
        val result = StringBuilder()
        var index = 0
        while (m.find()) {
            result.append(text.substring(index, m.start()))
            result.append(mergeSection(m.group()))
            index = m.end()
        }
        result.append(text.substring(index, text.length))
        return result.toString()

    }

    fun mergeSection(text: String): String {
        if (!MULTI_CHORD.matcher(text).matches()) {
            return text
        }
        var result = text.trim('[').trim(']')
        result = result.replace(("(" + Transposer.FULL_CHORD + ")").toRegex(), "[$1]")
        result = result.replace(",", "")
        return result
    }

    fun merge2(text: String): String {
        val lines = text.split("\n".toRegex())
        var result = StringBuilder()
        for (line in lines) {
            val newLine = line.replace(("\\[?(?<spacebefore>\\s*)(?<fullchord>" + Transposer.FULL_CHORD + ")(?<spaceafter>\\s*)\\]?").toRegex(), "\${spacebefore}[\${fullchord}]\${spaceafter}")
            result.append(newLine + "\n")
        }
        return result.toString().trimEnd()
    }

    fun removeChords(text: String): String {
        val result = StringBuilder()
        for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val newLine = merge(line).replace(CHORD_REGEX.toRegex(), "")
            if (line == newLine) {
                result.append(line + "\n")
            } else if (!newLine.trim { it <= ' ' }.isEmpty()) {
                result.append(newLine + "\n")
            }
        }
        return result.toString().trimEnd()
    }

    fun lineContainsChordsOnly(line: String): Boolean {
        return line.isNotBlank() && removeChords(line).trim().isEmpty()
    }

    fun lineContainsChordsAndText(line: String): Boolean {
        val lineChordsRemoved = removeChords(line).trim()
        return lineChordsRemoved.trim() != line.trim() && !lineChordsRemoved.isEmpty()
    }

    fun lineContainsTextOnly(line: String): Boolean {
        val lineChordsRemoved = removeChords(line).trim()
        return lineChordsRemoved.trim() == line.trim()
    }


    @JvmOverloads
    @JvmStatic
    fun removeDuplicateChordSequences(text: String, minimumSequenceCount: Int = MINIMUM_SEQUENCE_COUNT): String {
        val result = StringBuilder()
        val foundChords = HashSet<List<String>>()
        for (line in merge(text).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val chords = ChordDetector.getBracketChordsInLine(line).toList()
            if (chords.size >= minimumSequenceCount) {
                if (foundChords.contains(chords)) {
                    result.append(Transposer.removeChords(line) + "\n")
                } else {
                    result.append(line + "\n")
                }
                foundChords.add(chords)
            } else {
                result.append(line + "\n")
            }
        }
        return result.toString().trimEnd()
    }

    fun removeNonChords(text: String): String {
        val result = StringBuilder()
        val pattern = (CHORD_REGEX).toPattern()
        for (line in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val m = pattern.matcher(line)
            var found = false
            while (m.find()) {
                result.append(m.group())
                found = true
            }
            if (found) {
                result.append("\n")
            }
        }
        return result.toString().trimEnd()
    }

    private fun split(group: String): String {
        var result = ""
        for (s in group.split("[,\\s]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (s.length != 0) {
                result += "[$s]"
            }
        }
        return result
    }

}
