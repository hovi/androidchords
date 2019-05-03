package eu.karelhovorka.zpevnik.util



object Comments {
    fun markComments(text: String): String {
        return text.replace("^#(.*)$".toRegex(RegexOption.MULTILINE), "<comment>$1</comment>")
    }

    fun hideComments(text: String): String {
        //if whole text starts with #, we leave leading newline
        if (text.startsWith("#")) {
            return text.replace("(^|\n)#([^\n]*)".toRegex(), "").replace("^\n".toRegex(), "")
        }
        return text.replace("(^|\n)#([^\n]*)".toRegex(), "")
    }


}
