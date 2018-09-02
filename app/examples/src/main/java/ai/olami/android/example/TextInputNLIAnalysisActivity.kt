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

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.google.gson.Gson

import ai.olami.cloudService.APIConfiguration
import ai.olami.cloudService.APIResponse
import ai.olami.cloudService.TextRecognizer
import ai.olami.cloudService.NLIConfig
import ai.olami.util.GsonFactory

class TextInputNLIAnalysisActivity : AppCompatActivity() {

    private var textInputSubmitButton: Button? = null
    private var textInputEdit: EditText? = null
    private var textInputResponse: TextView? = null

    private var mJsonDump: Gson? = null
    private var mRecognizer: TextRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input_nli_analysis)

        val intent = intent
        Config.localizeOption = intent.getIntExtra("LOCALIZE_OPTION", Config.localizeOption)

        textInputSubmitButton = findViewById(R.id.textSubmitButton) as Button
        textInputSubmitButton!!.setOnClickListener(textInputSubmitButtonListener())
        textInputEdit = findViewById(R.id.textInputEditText) as EditText
        textInputResponse = findViewById(R.id.textInputNLIAPIResponse) as TextView
        textInputResponse!!.movementMethod = ScrollingMovementMethod.getInstance()

        mJsonDump = GsonFactory.getDebugGson(false)

        // * Step 1: Configure your key and localize option.
        val config = APIConfiguration(
                Config.appKey, Config.appSecret, Config.localizeOption)

        // * Step 2: Create the text recognizer.
        mRecognizer = TextRecognizer(config)
        mRecognizer!!.sdkType = "android"

        // * Optional steps: Setup some other configurations.
        mRecognizer!!.endUserIdentifier = "Someone"
        mRecognizer!!.timeout = 10000

    }

    private fun submitButtonChangeHandler(isEnabled: Boolean, buttonString: String) {
        Handler(this.mainLooper).post {
            textInputSubmitButton!!.isEnabled = isEnabled
            textInputSubmitButton!!.text = buttonString
        }
    }

    protected inner class textInputSubmitButtonListener : View.OnClickListener {
        override fun onClick(v: View) {
            submitButtonChangeHandler(false, getString(R.string.RecognizeState_PROCESSING) + "...")
            Thread(Runnable {
                try {
                    // * Send text to request NLI recognition.
                    val response = mRecognizer!!.requestNLI(textInputEdit!!.text.toString())
                    //
                    // You can also send text with NLIConfig to append "nli_config" JSON object.
                    //
                    // For Example,
                    // try to replace 'requestNLI(inputText)' with the following sample code:
                    // ===================================================================
                    // NLIConfig nliConfig = new NLIConfig();
                    // nliConfig.setSlotName("myslot");
                    // APIResponse response = mRecognizer.requestNLI(textInputEdit.getText().toString(), nliConfig);
                    // ===================================================================
                    //

                    // Check request status.
                    if (response.ok() && response.hasData()) {
                        // * Dump NLI results by JSON format.
                        textInputAPIResponseChangeHandler(mJsonDump!!.toJson(response))
                        // --------------------------------------------------
                        // * You can also handle or process NLI/IDS results ...
                        //
                        //   For example:
                        //   NLIResult[] nliResults = response.getData().getNLIResults();
                        //
                        // * See also :
                        //   - OLAMI Java Client SDK & Examples
                        //   - ai.olami.nli.NLIResult.
                        // --------------------------------------------------

                        submitButtonChangeHandler(true, getString(R.string.Submit))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }).start()
        }
    }

    private fun textInputAPIResponseChangeHandler(APIResponseDump: String) {
        Handler(this.mainLooper).post { textInputResponse!!.text = getString(R.string.Response) + " : \n" + APIResponseDump }
    }

    companion object {

        private val TAG = "TextInputNLIAnalysisActivity"
    }
}
