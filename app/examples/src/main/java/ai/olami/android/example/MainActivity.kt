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
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main_new.*

import ai.olami.cloudService.APIConfiguration
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
/*
    private val appKeyEditText: EditText? = null
    private val appSecretEditText: EditText? = null

    protected val systemlLocale: Locale
        get() {
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = resources.configuration.locales.get(0)
            } else {
                locale = resources.configuration.locale
            }
            return locale
        }

    protected val systemLanguage: String
        get() {
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LocaleList.getDefault().get(0)
            } else {
                locale = Locale.getDefault()
            }

            return locale.language + "-" + locale.country
        }
        */
/*
    private val ActivityListCick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val appKey = Config.appKey
        val appSecret = Config.appSecret
        if (appKey.isEmpty() || appKey.startsWith("*") || appSecret.isEmpty() || appSecret.startsWith("*")) {
            //onCreateConfigurationDialog().show()
        } else {

            val intent: Intent
            when (position) {
                0 -> {
                    // Start SpeechInput Activity
                    intent = Intent(this@MainActivity, SpeechInputActivity::class.java)
                    intent.putExtra("LOCALIZE_OPTION", Config.localizeOption)
                    startActivity(intent)
                }
            }
        }
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_new)

/*
        // Create example list
        val ActivityListView = findViewById(R.id.listview) as ListView
        val ActivityList = arrayOf(getString(R.string.SpeechInput))

        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                ActivityList)
        ActivityListView.adapter = adapter
        ActivityListView.onItemClickListener = ActivityListCick
        */
/*
        // load the default App-Key & App-Secret.
        val appKey = Config.appKey
        val appSecret = Config.appSecret
        if (appKey.isEmpty() || appKey.startsWith("*") || appSecret.isEmpty() || appSecret.startsWith("*")) {
            // If the developer doesn't change keys, pop up and the developer to input their keys.
            onCreateConfigurationDialog().show()
        }
*/

    // 設定成中文
    Config.localizeOption = APIConfiguration.LOCALIZE_OPTION_TRADITIONAL_CHINESE

    //設定日期及時間
    dateview.setText(SimpleDateFormat("MM/dd").format(Date()))
    val weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val week = arrayListOf("日","一","二","三","四","五","六")
    weekview.setText("("+ week[weekday-1]+ ")")


        speechButton.setOnClickListener{
            var speeIntent: Intent
            speeIntent = Intent(this@MainActivity, SpeechInputActivity::class.java)
            speeIntent.putExtra("LOCALIZE_OPTION", Config.localizeOption)
            startActivity(speeIntent)
        }
        medicineButton.setOnClickListener {
            var medicineIntent = Intent(this,medicineActivity::class.java)
            startActivity(medicineIntent)
        }

        emsButton.setOnClickListener {
            var emsIntent = Intent(this,emsActivity::class.java)
            startActivity(emsIntent)
        }

        foodButton.setOnClickListener {
            var foodIntent = Intent(this,foodActivity::class.java)
            startActivity(foodIntent)
        }

        actButton.setOnClickListener {
            var actIntent = Intent(this,actActivity::class.java)
            startActivity(actIntent)
        }

        easyButton.setOnClickListener{
            var easyIntent = Intent(this,easyActivity::class.java)
            startActivity(easyIntent)
        }
        settingButton.setOnClickListener {
            var settingIntent = Intent(this,loginActivity::class.java)
            startActivity(settingIntent)
        }
    }

/*
    fun onCreateConfigurationDialog(): Dialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.Input)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.configuration_setting, null)

        val appKeyInput = view.findViewById(R.id.appKeyInput) as EditText
        val appSecretInput = view.findViewById(R.id.appSecretInput) as EditText

        builder.setView(view)
                .setPositiveButton(R.string.Submit) { dialog, id ->
                    val userAppKeyInput = appKeyInput.text.toString()
                    val userAppSecret = appSecretInput.text.toString()

                    if (userAppKeyInput.isEmpty() || userAppSecret.isEmpty()) {
                        Toast.makeText(this@MainActivity, R.string.InputKeyIsEmpty, Toast.LENGTH_LONG).show()
                        onCreateConfigurationDialog().show()
                    } else {
                        // The developer has already inputted keys.
                        Config.appKey = userAppKeyInput
                        Config.appSecret = userAppSecret
                    }
                }
                .setNegativeButton(R.string.Register) { dialog, id ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://olami.ai"))
                    startActivity(intent)
                    onCreateConfigurationDialog().show()
                }
        return builder.create()
    } // 沒有IDㄉ
*/
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
*/
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        // Manually switch the localization setting.
        if (id == R.id.setting_changeCN) {
            Config.localizeOption = APIConfiguration.LOCALIZE_OPTION_SIMPLIFIED_CHINESE
            switchLanguage(this, "china")
        } else if (id == R.id.setting_changeTW) {
            Config.localizeOption = APIConfiguration.LOCALIZE_OPTION_TRADITIONAL_CHINESE
            switchLanguage(this, "taiwan")
        }

        return super.onOptionsItemSelected(item)
    }
    */
/*
    protected fun switchLanguage(context: Context, language: String) {
        val resources = resources
        val config = resources.configuration
        val dm = resources.displayMetrics
        if (language == "taiwan") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(TAIWAN)
            } else {
                config.locale = TAIWAN
            }
        } else {
            // default language: mainland
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(CHINA)
            } else {
                config.locale = CHINA
            }
        }
        context.resources.updateConfiguration(config, dm)
        val refresh = Intent(context, MainActivity::class.java)
        startActivity(refresh)
        finish()
    }
*/
}



