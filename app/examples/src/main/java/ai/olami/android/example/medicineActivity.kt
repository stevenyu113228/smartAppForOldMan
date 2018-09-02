package ai.olami.android.example

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_medicine.*
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.xml.sax.Parser

import org.json.JSONObject

class medicineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)
        setTitle("領藥功能")

        btn.setOnClickListener{
            var integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            //integrator.setPrompt("Scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                //tvresult.setText(result.getContents().toString())
                receiveQR(result.getContents().toString())
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private fun receiveQR(strrr: String){
        /*
        val jd=  JSONObject(strrr)
        var info = "藥品資訊:衛署藥製字第"+ jd.getString("id") +"號\n" +
                "藥品名稱:" + jd.getString("name") + "\n" +
                "藥品功效:" + jd.getString("efficacy") + "\n"+
                "使用方法:" + jd.getString("time")
        tvresult.setText(info)
        */
        eatDrugImImage.visibility  = View.VISIBLE
        eatDrugImImage6.visibility  = View.VISIBLE
        eatDrugImImage4.visibility = View.VISIBLE

    }
}
