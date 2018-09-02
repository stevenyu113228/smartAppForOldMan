/*
	Copyright 2017, VIA Technologies, Inc. & OLAMI Team.

	http://olami.ai

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package ai.olami.android.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

import ai.olami.android.IRecorderSpeechRecognizerListener
import ai.olami.android.RecorderSpeechRecognizer
import ai.olami.cloudService.APIConfiguration
import ai.olami.cloudService.APIResponse
import ai.olami.cloudService.SpeechResult
import ai.olami.cloudService.NLIConfig
import org.json.JSONObject


class SpeechInputActivity : AppCompatActivity() {

    internal var mRecognizer: RecorderSpeechRecognizer? = null

    private val VOLUME_BAR_MAX_VALUE = 40
    private val VOLUME_BAR_MAX_ITEM = 20
    private val VOLUME_BAR_ITEM_VALUE = VOLUME_BAR_MAX_VALUE / VOLUME_BAR_MAX_ITEM

    private var recordButton: Button? = null
    private var cancelButton: Button? = null

    private var voiceVolumeText: TextView? = null
    private var voiceVolumeBar: TextView? = null
    private var STTText: TextView? = null
    private var APIResponseText: TextView? = null
    private var recognizeStatusText: TextView? = null
    private var recordStatusText: TextView? = null

    private var mRecordState: RecorderSpeechRecognizer.RecordState? = null
    private var mRecognizeState: RecorderSpeechRecognizer.RecognizeState? = null

    private var mAutoStopSwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_input)
        setTitle("語音識別")

        val intent = intent
        Config.localizeOption = intent.getIntExtra("LOCALIZE_OPTION", Config.localizeOption)

        recordButton = findViewById(R.id.recordButton) as Button
        cancelButton = findViewById(R.id.cancelButton) as Button
        voiceVolumeText = findViewById(R.id.voiceVolume) as TextView
        voiceVolumeBar = findViewById(R.id.voiceVolumeBar) as TextView
        STTText = findViewById(R.id.STTText) as TextView
        APIResponseText = findViewById(R.id.APIResponse) as TextView
        APIResponseText!!.movementMethod = ScrollingMovementMethod.getInstance()
        recognizeStatusText = findViewById(R.id.recognizeStatus) as TextView
        recordStatusText = findViewById(R.id.recordStatus) as TextView

        recordButton!!.setOnClickListener(recordButtonListener())
        cancelButton!!.setOnClickListener(cancelButtonListener())

        mAutoStopSwitch = findViewById(R.id.autoStopSwitch) as Switch
        mAutoStopSwitch!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (mRecognizer != null) {
                mRecognizer!!.enableAutoStopRecording(isChecked)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if the user agrees to access the microphone
        val hasMicrophonePermission = checkApplicationPermissions(
                Manifest.permission.RECORD_AUDIO,
                REQUEST_MICROPHONE)

        if (hasMicrophonePermission) {
            // * Step 1: Configure your key and localize option.
            val config = APIConfiguration(
                    Config.appKey, Config.appSecret, Config.localizeOption)

            // * Step 2: Create the microphone recording speech recognizer.
            //           ----------------------------------------------------------
            //           You should implement the IRecorderSpeechRecognizerListener
            //           to get all callbacks and assign the instance of your
            //           listener class into this recognizer.
            mRecognizer = RecorderSpeechRecognizer.create(SpeechRecognizerListener(), config)

            // * Optional step: Setup the recognize result type of your request.
            //                  The default setting is RECOGNIZE_RESULT_TYPE_STT for Speech-To-Text.
            mRecognizer!!.setRecognizeResultType(RecorderSpeechRecognizer.RECOGNIZE_RESULT_TYPE_ALL)

            // * Other optional steps: Setup some other configurations.
            //                         You can use default settings without bellow steps.
            mRecognizer!!.setEndUserIdentifier("Someone")
            mRecognizer!!.setApiRequestTimeout(3000)

            // * Advanced setting example.
            //   These are also optional steps, so you can skip these
            //   (or any one of these) to use default setting(s).
            // ------------------------------------------------------------------
            // * You can set the length of end time of the VAD in milliseconds
            //   to stop voice recording automatically.
            mRecognizer!!.setLengthOfVADEnd(2000)
            // * You can set the frequency in milliseconds of the recognition
            //   result query, then the recognizer client will query the result
            //   once every milliseconds you set.
            mRecognizer!!.setResultQueryFrequency(100)
            // * You can set audio length in milliseconds to upload, then
            //   the recognizer client will upload parts of audio once every
            //   milliseconds you set.
            mRecognizer!!.setSpeechUploadLength(300)
            // * Due to the different microphone sensitivity of each different device,
            //   you can set level of silence volume of the VAD
            //   to stop voice recording automatically.
            //   The recommended value is 5 to 10.
            mRecognizer!!.setSilenceLevelOfVADTail(5)
            // ------------------------------------------------------------------

            // Initialize volume bar of the input audio.
            voiceVolumeChangeHandler(0)

            if (mRecognizer!!.isAutoStopRecordingEnabled) {
                autoStopSwitchChangeHandler(true)
            } else {
                autoStopSwitchChangeHandler(false)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // * Release the recognizer when program stops or exits.
        if (mRecognizer != null) {
            mRecognizer!!.release()
        }
    }

    protected inner class recordButtonListener : View.OnClickListener {
        override fun onClick(v: View) {
            // Get current voice recording state.
            mRecordState = mRecognizer!!.recordState

            // Check to see if we should start recording or stop manually.
            if (mRecordState == RecorderSpeechRecognizer.RecordState.STOPPED) {
                try {

                    // * Request to start voice recording and recognition.
                    mRecognizer!!.start()
                    //
                    // You can also send text with NLIConfig to append "nli_config" JSON object.
                    //
                    // For Example, try to replace 'start()' with the following sample code:
                    // ===================================================================
                    // NLIConfig nliConfig = new NLIConfig();
                    // nliConfig.setSlotName("myslot");
                    // mRecognizer.start(nliConfig);
                    // ===================================================================
                    //

                } catch (e: InterruptedException) {

                    e.printStackTrace()

                }

                recordButton!!.isEnabled = false

            } else if (mRecordState == RecorderSpeechRecognizer.RecordState.RECORDING) {

                // * Request to stop voice recording when manually stop,
                //   and then wait for the final recognition result.
                mRecognizer!!.stop()

            }
        }
    }

    private inner class cancelButtonListener : View.OnClickListener {
        override fun onClick(v: View) {

            // * Issue to cancel all process including voice recording
            //   and speech recognition.
            mRecognizer!!.cancel()

        }
    }

    /**
     * This is a callback listener example.
     *
     * You should implement the IRecorderSpeechRecognizerListener
     * to get all callbacks and assign the instance of your listener class
     * into the recognizer instance of RecorderSpeechRecognizer.
     */
    private inner class SpeechRecognizerListener : IRecorderSpeechRecognizerListener {

        // * Implement override method to get callback when the voice recording
        //   process state changes.
        override fun onRecordStateChange(state: RecorderSpeechRecognizer.RecordState) {
            var StatusStr = getString(R.string.RecordState) + " : "
            mRecordState = state

            if (state == RecorderSpeechRecognizer.RecordState.STOPPED) {

                // * The recording process is stopped.
                // * This is also the beginning or end of the life cycle.

                StatusStr += getString(R.string.RecordState_STOPPED)
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                //recordButtonChangeHandler(true, getString(R.string.recordButton_start))
                cancelButtonChangeHandler(View.INVISIBLE, "")

            } else if (state == RecorderSpeechRecognizer.RecordState.INITIALIZING) {

                // * The recording process is initializing.
                // * This is normally starts after the STOPPED state.

                StatusStr += getString(R.string.RecordState_INITIALIZING) + "..."
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                recordButtonChangeHandler(false, StatusStr)
                cancelButtonChangeHandler(View.INVISIBLE, "")
                APIResponseChangeHandler("")
                STTChangeHandler("")
                APIResponseChangeHandler("")

            } else if (state == RecorderSpeechRecognizer.RecordState.INITIALIZED) {

                // * The recording process is initialized.
                // * This is normally starts after the INITIALIZING state.

                StatusStr += getString(R.string.RecordState_INITIALIZED)
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                recordButtonChangeHandler(false, StatusStr)
                cancelButtonChangeHandler(View.INVISIBLE, "")

            } else if (state == RecorderSpeechRecognizer.RecordState.RECORDING) {

                // * The recording process is starting.
                // * This is normally starts after the INITIALIZED state.

                StatusStr += getString(R.string.RecordState_RECORDING) + "..."
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                //recordButtonChangeHandler(true, getString(R.string.recordButton_stop))
                //cancelButtonChangeHandler(View.VISIBLE, "X")

            } else if (state == RecorderSpeechRecognizer.RecordState.STOPPING) {

                // * The recording process is stopping.
                // * This is normally starts after the RECORDING state
                // * and the next state should be STOPPED.
                // * --------------------------------------------------------
                //   This DOES NOT mean that the speech recognition process
                //   is also being stopped.
                // * --------------------------------------------------------

                StatusStr += getString(R.string.RecordState_STOPPING) + "..."
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                recordButtonChangeHandler(false, StatusStr)
                //cancelButtonChangeHandler(View.VISIBLE, "X")
                voiceVolumeChangeHandler(0)

            } else if (state == RecorderSpeechRecognizer.RecordState.ERROR) {

                // * There was an error in the recording process.

                StatusStr += getString(R.string.RecordState_ERROR)
                Log.i(TAG, StatusStr)
                recordStateHandler(StatusStr)
                recordButtonChangeHandler(false, StatusStr)
                //cancelButtonChangeHandler(View.VISIBLE, "X")
                voiceVolumeChangeHandler(0)
                errorStateHandler(StatusStr)

            }
        }

        // * Implement override method to get callback when the recognize
        //   process state changes.
        override fun onRecognizeStateChange(state: RecorderSpeechRecognizer.RecognizeState) {
            var StatusStr = getString(R.string.RecognizeState) + " : "
            mRecognizeState = state

            if (state == RecorderSpeechRecognizer.RecognizeState.STOPPED) {

                // * The recognize process is stopped.
                // * This is also the beginning or end of the life cycle.

                StatusStr += getString(R.string.RecognizeState_STOPPED)
                Log.i(TAG, StatusStr)
                recognizeStateHandler(StatusStr)

            } else if (state == RecorderSpeechRecognizer.RecognizeState.PROCESSING) {

                // * The recognize process is start running.
                // * This is normally starts after the STOPPED state.

                StatusStr += getString(R.string.RecognizeState_PROCESSING) + "..."
                Log.i(TAG, StatusStr)
                recognizeStateHandler(StatusStr)

            } else if (state == RecorderSpeechRecognizer.RecognizeState.COMPLETED) {

                // * The recognize process is start running.
                // * This is normally starts after the PROCESSING state.
                // * and the next state should be STOPPED.
                // * --------------------------------------------------------
                //   It means that a complete voice recognition has been done.
                //   So it usually triggers
                //   onRecognizeResultChange(APIResponse response) callback,
                //   then you will get complete results including the content
                //   of speech-to-text, NLI or IDS data in that callback.
                // * --------------------------------------------------------

                StatusStr += getString(R.string.RecognizeState_COMPLETED)
                Log.i(TAG, StatusStr)
                recognizeStateHandler(StatusStr)

            } else if (state == RecorderSpeechRecognizer.RecognizeState.ERROR) {

                // * There was an error in the recognize process.

                StatusStr += getString(R.string.RecognizeState_ERROR)
                Log.i(TAG, StatusStr)
                recognizeStateHandler(StatusStr)
                errorStateHandler(StatusStr)

            }
        }

        // * Implement override method to get callback when the results
        //   of speech recognition changes.
        override fun onRecognizeResultChange(response: APIResponse) {

            // * Get recognition results.
            //   In this example, we only handle the speech-to-text result.
            val sttResult = response.data.speechResult

            if (sttResult.complete()) {

                // 'complete() == true' means returned text is final result.

                // --------------------------------------------------
                // * It also means you can get NLI/IDS results if included.
                //   So you can handle or process NLI/IDS results here ...
                //
                //   For example:
                //   NLIResult[] nliResults = response.getData().getNLIResults();
                //
                // * See also :
                //   - OLAMI Java Client SDK & Examples
                //   - ai.olami.nli.NLIResult.
                // --------------------------------------------------

                STTChangeHandler(sttResult.result)
                APIResponseChangeHandler(response.toString())

            } else {

                // Recognition has not yet been completed.
                // The text you get here is not a final result.

                if (sttResult.status == SpeechResult.STATUS_RECOGNIZE_OK) {
                    STTChangeHandler(sttResult.result)
                    APIResponseChangeHandler(response.toString())
                }

            }
        }

        // * Implement override method to get callback when the volume of
        //   voice input changes.
        override fun onRecordVolumeChange(volumeValue: Int) {

            // Do something here when you get the changed volume.

            voiceVolumeChangeHandler(volumeValue)
        }

        // * Implement override method to get callback when server error occurs.
        override fun onServerError(response: APIResponse) {
            Log.e(TAG, "Server error code: " + response.errorCode
                    + ", Error message: " + response.errorMessage)
            errorStateHandler("onServerError Code: " + response.errorCode)
        }

        // * Implement override method to get callback when error occurs.
        override fun onError(error: RecorderSpeechRecognizer.Error) {
            Log.e(TAG, "Error code:" + error.name)
            errorStateHandler("RecorderSpeechRecognizer.Error: " + error.name)
        }

        // * Implement override method to get callback when exception occurs.
        override fun onException(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun recordButtonChangeHandler(isEnabled: Boolean, buttonString: String) {
        Handler(this.mainLooper).post {
            recordButton!!.isEnabled = isEnabled
            recordButton!!.text = buttonString
        }
    }

    private fun cancelButtonChangeHandler(isVisibility: Int, buttonString: String) {
        Handler(this.mainLooper).post {
            cancelButton!!.visibility = isVisibility
            cancelButton!!.text = buttonString
        }
    }

    private fun voiceVolumeChangeHandler(volume: Int) {
        val volumeBarItemCount = volume / VOLUME_BAR_ITEM_VALUE

        Handler(this.mainLooper).post {
            voiceVolumeText!!.text = getString(R.string.Volume) + " : " + volume
            // Voice volume bar value change
            var voiceVolumeBarStr = "▌"
            var i = 1
            while (i < volumeBarItemCount && i <= VOLUME_BAR_MAX_ITEM) {
                voiceVolumeBarStr += "▌"
                i++
            }
            voiceVolumeBar!!.text = voiceVolumeBarStr

            // Voice volume bar color change
            if (volumeBarItemCount >= 0 && volumeBarItemCount <= 7) {
                voiceVolumeBar!!.setTextColor(Color.GREEN)
            } else if (volumeBarItemCount >= 7 && volumeBarItemCount <= 14) {
                voiceVolumeBar!!.setTextColor(Color.BLUE)
            } else {
                voiceVolumeBar!!.setTextColor(Color.RED)
            }
        }
    }

    private fun STTChangeHandler(STTStr: String) {
        Handler(this.mainLooper).post {
            STTText!!.text = STTStr
            var changeIntent:Intent
            if ("藥" in STTStr){
                changeIntent = Intent(this,medicineActivity::class.java)
                startActivity(changeIntent)
                this.finish()
            }
            else if ("活動" in STTStr) {
                changeIntent = Intent(this,actActivity::class.java)
                startActivity(changeIntent)
                this.finish()
            }
            else if ("吃" in STTStr){
                changeIntent = Intent(this,foodActivity::class.java)
                startActivity(changeIntent)
                this.finish()
            }
            else if("救護車" in STTStr){
                changeIntent = Intent(this,emsActivity::class.java)
                startActivity(changeIntent)
                this.finish()
            }
            else if("運動" in STTStr){
                changeIntent = Intent(this,emsActivity::class.java)
                startActivity(changeIntent)
                this.finish()
            }

        }
    }

    private fun APIResponseChangeHandler(APIResponseStr: String) {
        Handler(this.mainLooper).post {
            try {
                var jd = JSONObject(APIResponseStr)
                var reply = jd.getJSONObject("data").getJSONArray("nli")
                        .getJSONObject(0).getJSONObject("desc_obj").getString("result")
                APIResponseText!!.text = reply
            }catch (e: Exception){
                APIResponseText!!.text = ""
            }
        }
    }

    private fun recognizeStateHandler(recognizeStatusStr: String) {
        Handler(this.mainLooper).post { recognizeStatusText!!.text = recognizeStatusStr }
    }

    private fun recordStateHandler(recordStatusStr: String) {
        Handler(this.mainLooper).post { recordStatusText!!.text = recordStatusStr }
    }

    private fun errorStateHandler(errorString: String) {
        Handler(this.mainLooper).post {
            Toast.makeText(applicationContext,
                    errorString,
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun autoStopSwitchChangeHandler(isChecked: Boolean) {
        Handler(this.mainLooper).post { mAutoStopSwitch!!.isChecked = isChecked }
    }

    private fun checkApplicationPermissions(permissionStr: String, requestCode: Int): Boolean {
        // Check to see if we have permission to access something,
        // such like the microphone.
        val permission = ActivityCompat.checkSelfPermission(
                this@SpeechInputActivity,
                permissionStr)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We can not access it, request authorization from the user.
            ActivityCompat.requestPermissions(
                    this@SpeechInputActivity,
                    arrayOf(permissionStr),
                    requestCode
            )
            return false
        } else {
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_MICROPHONE -> for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.RECORD_AUDIO) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(
                                this,
                                getString(R.string.GetMicrophonePermission),
                                Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this,
                                getString(R.string.GetMicrophonePermissionDenied),
                                Toast.LENGTH_LONG).show()
                    }
                }
            }
            REQUEST_EXTERNAL_PERMISSION -> for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(
                                this,
                                getString(R.string.GetWriteStoragePermission),
                                Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this,
                                getString(R.string.GetWriteStoragePermissionDenied),
                                Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    companion object {

        private val TAG = "SpeechInputActivity"

        private val REQUEST_EXTERNAL_PERMISSION = 1
        private val REQUEST_MICROPHONE = 3
    }

}
