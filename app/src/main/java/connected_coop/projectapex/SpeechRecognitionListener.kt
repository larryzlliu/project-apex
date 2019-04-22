package connected_coop.projectapex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.*

class SpeechRecognitionListener(context: Context, val processedTextlistener: (String) -> Unit) {

    companion object {
        private const val TIME_UNTIL_TIMEOUT = 50000
        private const val MS_TO_SECONDS = 1000
    }

    // Speech to text:

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var startTime: Long = -1
    private var speechText: String? = null
    private var totalCurrentSpeechDuration: Float = 0.0f

    init {
        speechRecognizer.setRecognitionListener(ApexRecognitionListener())
    }

    fun startListening() {
        speechRecognizer.startListening(createRecognitionIntent())
        startTime = System.currentTimeMillis()
    }

    fun stopListening() {
        speechRecognizer.stopListening()
        totalCurrentSpeechDuration = ((System.currentTimeMillis() - startTime)/MS_TO_SECONDS).toFloat()
    }

    fun discard() {
        speechRecognizer.stopListening()
        totalCurrentSpeechDuration = 0f
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

    inner class ApexRecognitionListener: RecognitionListener {
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
            speechText = speechToText
            processedTextlistener.invoke(speechToText)
        }
    }
}
