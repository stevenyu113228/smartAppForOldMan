package ai.olami.android.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_act_reg.*
import org.json.JSONObject

class actRegActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_reg)
        setTitle("詳細資料")
        var ininput:String ="{\"Data\":{\"id\": \"1\", \"name\": \"花草樂趣  好生活\", \"date\": \"9/5\n(三)\", \"time\": \"10:00~12:00\", \"address\": \"台灣大道二段659號  4樓4-2教室\", \"teacher\": \"王麗卿  講師\", \"price\": \"1200元整\"}}"



        //--------------------------------------------------------------------------
        fun jsonOne(input: String, find: String): String {
            var j: JSONObject
            j = JSONObject(input)
            var jsonOb = j.getJSONObject("Data").getString(find)

            return jsonOb
        }

        textView12.setText(jsonOne(ininput,"name"))
        textView13.setText(jsonOne(ininput,"date"))
        textView2b.setText(jsonOne(ininput,"time"))
        textView4b.setText(jsonOne(ininput,"price"))
        textView6b.setText(jsonOne(ininput,"address"))

    }
}
