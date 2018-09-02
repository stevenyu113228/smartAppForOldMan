package ai.olami.android.example

import ai.olami.android.example.Config.myurl
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import kotlinx.android.synthetic.main.activity_act.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class actActivity : AppCompatActivity() {
    //val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act)
        setTitle("樂齡活動")

        var ininput = arrayOfNulls<String>(5)
        ininput[0] ="{\"Data\":{\"id\": \"1\", \"name\": \"花草樂趣\n好生活\", \"date\": \"9/5\n(三)\", \"time\": \"上午10時  至12時\", \"address\": \"台灣大道二段659號  4樓4-2教室\", \"teacher\": \"王麗卿  講師\", \"price\": \"課程費用  1200元整\"}}"
        ininput[1] ="{\"Data\":{\"id\": \"2\", \"name\": \"烏克麗麗\n進階班\", \"date\": \"9/8\n(六)\", \"time\": \"下午2時  至4時\", \"address\": \"台灣大道二段659號  12樓12-1教室\", \"teacher\": \"林欣慧  講師\", \"price\": \"報名費  1000元整\"}}"
        ininput[2] ="{\"Data\":{\"id\": \"3\", \"name\": \"樂齡體適能\", \"date\": \"9/10\n(一)\", \"time\": \"上午10時  至  12時\", \"address\": \"台灣大道二段659號  12樓12-1教室\", \"teacher\": \"岳一萍  講師\", \"price\": \"報名費  1000元整\"}}"
        ininput[3] ="{\"Data\":{\"id\": \"4\", \"name\": \"腦力激盪\n智慧無限\", \"date\": \"9/20\n(四)\", \"time\": \"下午2時  至4時\", \"address\": \"台灣大道二段659號  4樓4-3教室\", \"teacher\": \"黃淑芬  講師\", \"price\": \"保證金500整\"}}"
        ininput[4] ="{\"Data\":{\"id\": \"5\", \"name\": \"烏克麗麗\", \"date\": \"9/25\n(二)\", \"time\": \"上午10時  至12時\", \"address\": \"台灣大道二段659號  4樓4-2教室\", \"teacher\": \"鄭美秀  講師\", \"price\": \"報名費  1000元整\"}}"



        //--------------------------------------------------------------------------
        fun jsonOne(input: String, find: String): String {
            var j: JSONObject
            j = JSONObject(input)
            var jsonOb = j.getJSONObject("Data").getString(find)

            return jsonOb
        }

        //--------------------------------------------------------------------------
        fun jsonArray(input: String, find: String): String {
            var j: JSONObject
            j = JSONObject(input)
            var jsonOb = j.getJSONObject("Data").getJSONArray(find)

            var temp: String = ""
            var tempnum: Int = jsonOb.length()

            temp += jsonOb[0]
            var i = 1
            while (i < tempnum) {
                temp += "\n" + jsonOb[i]
                i++
            }
            return temp
        }


        textView0b.setText(jsonOne(ininput[0]!!,"name"))
        textView2b.setText(jsonOne(ininput[1]!!,"name"))
        textView4b.setText(jsonOne(ininput[2]!!,"name"))
        textView6b.setText(jsonOne(ininput[3]!!,"name"))
        textView8b.setText(jsonOne(ininput[4]!!,"name"))

        textView1.setText(jsonOne(ininput[0]!!,"date"))
        textView3.setText(jsonOne(ininput[1]!!,"date"))
        textView5.setText(jsonOne(ininput[2]!!,"date"))
        textView7.setText(jsonOne(ininput[3]!!,"date"))
        textView9.setText(jsonOne(ininput[4]!!,"date"))

        imageButton1.setOnClickListener{
            val regIntent = Intent(this,actRegActivity::class.java)
            startActivity(regIntent)
        }
    }
        /*
        setContentView(R.layout.activity_act)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setTitle("樂齡活動")

        //button.setOnClickListener {
            val json = JSONObject()
            json.put("intent","activity")
            val i = post("https://" + myurl + "/api/",json)
            val rp = JSONObject(i)
            var ii = ""
            for(i in 1..8){
                 ii += rp.getJSONArray("data").getJSONArray(i).toString() + "\n"
            }
            text.setText(ii)
       // }
    }
    fun post(url:String, json: JSONObject):String{
        var body: RequestBody
            body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("intent", json.getString("intent"))
                    .build()
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }
    */
}
