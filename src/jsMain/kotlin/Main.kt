import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    println("HELLO WORLD CHORDS")
    var songText = document.querySelector("source-song-text")!!.innerHTML
    var songTitle = document.querySelector("source-song-title")!!.innerHTML
    println(songText)
    println(songTitle)
}

