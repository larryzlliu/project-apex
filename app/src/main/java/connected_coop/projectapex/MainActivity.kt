package connected_coop.projectapex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        val mSpeechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault())
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                50000)

        // TODO: extract to separate class
        mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onRmsChanged(v: Float) {}

            override fun onBufferReceived(bytes: ByteArray?) {}

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(i: Int, bundle: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {
                // TODO: maybe some pop up modal
            }

            override fun onResults(bundle: Bundle?) {
                val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!
                this@MainActivity.text_view.text = matches[0]
            }
        })

        button_record_toggle.setOnCheckedChangeListener { _, isRecording ->
            if (isRecording) {
                Toast.makeText(this, "Recording...", Toast.LENGTH_LONG).show()
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent)
            } else {
                Toast.makeText(this, "Stopping...", Toast.LENGTH_LONG).show()
                mSpeechRecognizer.stopListening()
            }
        }

        result_button.setOnClickListener { _ ->
            Toast.makeText(this, "starting activity", Toast.LENGTH_SHORT).show()
            // TODO: start result activity
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + packageName))
            startActivity(intent)
            finish()
        }

    }

}

