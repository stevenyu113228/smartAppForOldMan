package ai.olami.android.example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*


class settingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        myAdapter.addAll("登入", "姓名設定","健康紀錄")
        mainListview.setAdapter(myAdapter)

        mainListview.isClickable = true
        mainListview.setOnItemClickListener {adapterView: AdapterView<*>?,
                                             view: View?, position: Int, l: Long ->
            println(position)
            when(position){
                0 ->{
                    println("登入")
                    var loginIntent = Intent(this,loginActivity::class.java)
                    startActivity(loginIntent)
                }
                1 ->{
                    println("姓名設定")
                }
                2 ->{
                    println("健康紀錄")
                }
                else ->{
                    println("Error!!")
                }
            }
        }


    }
}
