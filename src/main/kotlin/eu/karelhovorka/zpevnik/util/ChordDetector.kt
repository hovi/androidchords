package eu.karelhovorka.zpevnik.util


import eu.karelhovorka.zpevnik.util.Preconditions.checkNotNull
import mock.JvmStatic
import mock.toPattern

object ChordDetector {


    @JvmStatic
    fun hasBracketChords(content: String): Boolean {
        checkNotNull(content, "content")
        val lines = content.split("[\n\r]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            if (hasBracketChordsInline(line)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun hasPlainChords(content: String): Boolean {
        checkNotNull(content, "content")
        val lines = content.split("[\n\r]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            if (hasPlainChordsInline(line)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun replacePlainChords(content: String): String {
        checkNotNull(content, "content")
        val sb = StringBuilder()
        val lines = content.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            sb.append(replacePlainChordsInline(line) + "\n")
        }
        return sb.toString().trimEnd()

    }

    fun replacePlainChordsInline(content: String): String {
        if (hasPlainChordsInline(content)) {
            return content.replace(("(" + Transposer.FULL_CHORD + ")").toRegex(), "[$1]")
        }
        return content
    }

    private fun hasPlainChordsInline(line: String): Boolean {
        checkNotNull(line, "line")
        var result = (line + " ").replace((Transposer.FULL_CHORD + "[\\s\\-,]").toRegex(), " ")
        result = result.replace("[,-]\\s".toRegex(), " ")
        result = result.replace("[0-9]x\\s".toRegex(), " ")
        return result.trim { it <= ' ' }.length == 0 && line.trim { it <= ' ' }.length > 0
    }

    private fun hasBracketChordsInline(line: String): Boolean {
        checkNotNull(line, "line")
        var result = (line + " ").replace(("\\[" + Transposer.FULL_CHORD + "\\]").toRegex(), " ")
        result = result.replace("[,-]\\s".toRegex(), " ")
        result = result.replace("[0-9]x\\s".toRegex(), " ")
        return result.trim { it <= ' ' }.length == 0 && line.trim { it <= ' ' }.length > 0
    }

    fun getBracketChordsInLine(line: String): Array<String> {
        checkNotNull(line, "line")
        val bracketChord = ("\\[(" + Transposer.FULL_CHORD + ")\\]").toPattern()
        val matcher = bracketChord.matcher(line)
        val chords = ArrayList<String>()
        while (matcher.find()) {
            chords.add(matcher.group(1)!!)
        }
        return chords.toTypedArray()
    }

    fun lineContainsChords(line: String, chords: Array<String>): Boolean {
        checkNotNull(line, "line")
        checkNotNull(chords, "chords")
        return (getBracketChordsInLine(line).contentEquals(chords))
    }

}
