package ai.olami.android.example

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ems.*

class emsActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ems)
        setTitle("緊急照護")
        ambulanceButton.setOnClickListener {
            var ambulanceIntent = Intent(Intent.ACTION_CALL)
            ambulanceIntent.setData(Uri.parse("tel:" + 112))
            startActivity(ambulanceIntent)
        }
    }
}
