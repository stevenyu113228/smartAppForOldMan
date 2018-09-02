package ai.olami.android.example

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_easy.*
import java.net.URI

class easyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy)
        setTitle("簡易運動")

        videoButton.setOnClickListener{
            val uri = Uri.parse("https://www.youtube.com/watch?v=9P8CDMj6qgw")
            val videoIntent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(videoIntent)
        }
    }
}
