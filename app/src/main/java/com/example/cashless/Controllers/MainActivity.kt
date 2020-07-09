package com.example.cashless.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cashless.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getStartedBtn.setOnClickListener{
//            if(FirebaseAuth.currentUser==null)
//            {
//                startActivity(Intent(this,LoginActivity::class.java))
//            }

            startActivity(Intent(this,
                LoginActivity::class.java))
        }
    }
}