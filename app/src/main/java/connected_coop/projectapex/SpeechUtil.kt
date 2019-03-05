package connected_coop.projectapex

class SpeechUtil() {
    private val SECONDS_TO_MIN = 60

    fun calculateWPM(speechText: String, speechDuration: Float): Float {
        val numWords = speechText.split("\\s+").size
        return numWords / speechDuration / SECONDS_TO_MIN
    }
}