package connected_coop.projectapex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.*

class ApexSpeechUtil(context: Context, processedTextlistener: (String) -> Unit) {

    companion object {
        private const val TIME_UNTIL_TIMEOUT = 50000
        private const val MS_TO_SECONDS = 1000
        private const val SECONDS_TO_MIN = 60
    }

    // Speech to text:

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var startTime: Long = -1
    private var totalCurrentSpeechDuration: Float = 0.0f
    private var speechText: String = ""

    init {

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onRmsChanged(v: Float) {}

            override fun onBufferReceived(bytes: ByteArray?) {}

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(i: Int, bundle: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {}

            override fun onResults(bundle: Bundle?) {
                val speechToText = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)[0]
                processedTextlistener.invoke(speechToText)
            }

        })
    }

    fun startListening() {
        speechRecognizer.startListening(createRecognitionIntent())
        startTime = System.currentTimeMillis()
    }

    fun stopListening() {
        speechRecognizer.stopListening()
        totalCurrentSpeechDuration = ((System.currentTimeMillis() - startTime)/MS_TO_SECONDS).toFloat()
    }

    // Metrics:

    fun calculateWPM(): Float {
        if(speechText.isNullOrBlank()) {
            throw RuntimeException("You need to have recorded a speech in order to calculate the WPM!")
        }
        val numWords = speechText.split("\\s+").size
        return numWords / totalCurrentSpeechDuration / SECONDS_TO_MIN
    }

    fun getTotalSpeechDuration(): Float {
        return totalCurrentSpeechDuration
    }

    private fun createRecognitionIntent(): Intent {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault())
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                TIME_UNTIL_TIMEOUT)
        return speechRecognizerIntent
    }

}