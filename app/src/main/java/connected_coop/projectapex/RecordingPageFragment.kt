package connected_coop.projectapex


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat.getDrawable
import android.support.v7.widget.AppCompatImageButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_recording.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RecordingPageFragment : Fragment() {
    private lateinit var speechListener : SpeechRecognitionListener
    private val speechUtil = SpeechUtil()
    private var speechText : String = ""
    private var currentPausedTime: Long = 0
    private var isRecording = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        checkPermission()

        val view = inflater.inflate(R.layout.fragment_recording, container,false)

        speechListener = SpeechRecognitionListener(context = activity, processedTextlistener = this::setTextView)

        val recordToggleButton = view.findViewById<AppCompatImageButton>(R.id.button_record_toggle)

        recordToggleButton.setOnClickListener {
            toggleRecording()
            if (isRecording) {
                Toast.makeText(activity, "Recording...", Toast.LENGTH_LONG).show()
                speechListener.startListening()
            } else {
                Toast.makeText(activity, "Stopping...", Toast.LENGTH_LONG).show()
                speechListener.stopListening()
            }
        }

        val discardButton = view.findViewById<Button>(R.id.discard_button)

        discardButton.setOnClickListener {
            speechText = ""
            text_view.text = ""
            recording_state.text = ""
            record_time.base = 0
            currentPausedTime = 0
            record_time.base = SystemClock.elapsedRealtime()
            speechListener.discard()
        }

        val continueButton = view.findViewById<Button>(R.id.continue_button)

        continueButton.setOnClickListener{
            val intent = Intent(activity, ResultsActivity::class.java)
            //add speechListener util to intent
            startActivity(intent)
        }

        return view
    }

    private fun toggleRecording() {
        isRecording = !isRecording
        if (isRecording) {
            button_record_toggle.background = getDrawable(resources, R.drawable.round_button_recording, null)
            recording_state.text = "recording"
            recording_state.setTextColor(Color.RED)
            record_time.base = SystemClock.elapsedRealtime() - currentPausedTime
            record_time.start()
        } else {
            button_record_toggle.background = getDrawable(resources, R.drawable.round_button, null)
            recording_state.text = "paused"
            // add theme to getColor so it isn't depricated
            recording_state.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            record_time.stop()
            currentPausedTime = SystemClock.elapsedRealtime() - record_time.base

        }
    }

    private fun setTextView(speechText: String) {
        text_view.text = speechText
    }

    //update to prompt user to update permissions
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + activity.packageName))
            startActivity(intent)
            activity.finish()
        }
    }
}
