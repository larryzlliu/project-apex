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

    private val speechUtil = ApexSpeechUtil(context = this, processedTextlistener = this::setTextView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        button_record_toggle.setOnCheckedChangeListener { _ , isRecording ->
            if (isRecording) {
                Toast.makeText(this, "Recording...", Toast.LENGTH_LONG).show()
                speechUtil.startListening()
            } else {
                Toast.makeText(this, "Stopping...", Toast.LENGTH_LONG).show()
                speechUtil.stopListening()
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

    private fun setTextView(speechText: String) {
        text_view.text = speechText
    }

}

