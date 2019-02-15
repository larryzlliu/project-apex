package connected_coop.projectapex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRecord = findViewById<ToggleButton>(R.id.button_record_toggle)

        btnRecord.setOnCheckedChangeListener{_, isRecording ->
            if (isRecording) {
                Toast.makeText(this, "Recording...", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Stopping...", Toast.LENGTH_LONG).show()
            }
        }
    }

}
