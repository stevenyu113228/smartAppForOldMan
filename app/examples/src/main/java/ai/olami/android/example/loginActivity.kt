package ai.olami.android.example

import ai.olami.android.example.Config.myurl
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.R.attr.data
import android.R.attr.visible
import android.os.CountDownTimer
import android.os.StrictMode
import android.support.v4.app.NotificationCompat.getExtras
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_act.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class loginActivity : AppCompatActivity() {
    val CAMERA_REQUEST_CODE = 0
    // 定義的 CAMERA_REQUEST_CODE
    val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
    // 定義要求存取寫入權限的REQUEST_CODE
    val RESULT_CROP_PHOTO_CODE = 2
    lateinit var imageUri: Uri
// 定義裁切照片ㄉREQUEST_CODE

    val client = OkHttpClient()
    var imgfile:File? = null


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTitle("帳號登入")
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        cameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, 0)
        }
        /*
        yButton.setOnClickListener {
            //checkResult()
        }
        nButton.setOnClickListener {
            //timer(30000,1000).start()
        }
        */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            0->{
                val extras = data!!.getExtras()
                val bmp = extras.get("data") as Bitmap
                cameraImageView.setImageBitmap(bmp)
                imgfile = saveImage(bmp)
                uploadImage(imgfile)
                timer(30000,1000).start()

            }
        }
    }

    private fun saveImage(finalBitmap: Bitmap):File{
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/saved_images")
        myDir.mkdirs()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            println(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }



    fun uploadImage(file:File?):String{
        var body: RequestBody
        val img_body = RequestBody.create(MediaType.parse("image/jpg"), file)
        body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("intent", "photo")
                .addFormDataPart("img", "xxx.jpg", img_body)
                .build()

        val request = Request.Builder()
                .url("https://" + myurl + "/api/")
                .post(body)
                .build()
        var res = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e)
                //client.newCall(call.request()).enqueue(this);

            }
            override fun onResponse(call: Call, response: Response) {
                val res_string = response.body()?.string()
                println(res_string)
                //res = response.body()?.string()!!
                //textResponse.setText(res_string)
            }
        })

     return res
    }
/*
    fun checkResult():String{
        var body: RequestBody
        body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("intent", "check")
                .build()

        val request = Request.Builder()
                .url("https://" + myurl + "/api/")
                .post(body)
                .build()
        var res = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e)
                //client.newCall(call.request()).enqueue(this);

            }
            override fun onResponse(call: Call, response: Response) {
                val res_string = response.body()?.string()
                println(res_string)
                res = res_string!!
                //textResponse.setText(res_string)
            }
        })

        return res

    }
*/

    fun checkResult():String{
        var body: RequestBody
        body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("intent", "check")
                .build()

        val request = Request.Builder()
                .url("https://" + myurl + "/api/")
                .post(body)
                .build()

        val response = client.newCall(request).execute()


        return response.body()!!.string()

    }
    private fun timer(millisInFuture:Long,countDownInterval:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                val timeRemaining = "${millisUntilFinished/1000}"
                timerText.text = timeRemaining
                }
            override fun onFinish() {
                //timerText.text = "Done"
                var res = checkResult()
                timerText.text = res

            }
        }

    }


}
