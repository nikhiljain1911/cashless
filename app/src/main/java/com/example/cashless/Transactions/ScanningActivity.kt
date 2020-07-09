package com.example.cashless.Transactions

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cashless.R
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.cashless.Constants
import com.example.cashless.Constants.Companion.MY_CAMERA_PERMISSION
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scanning.*




class ScanningActivity : AppCompatActivity() {
    lateinit var codeScanner:CodeScanner
    val MY_CAMERA_PERMISSION_REQUEST=1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning)

        codeScanner= CodeScanner(this,scannerView)
        codeScanner.camera=CodeScanner.CAMERA_BACK
        codeScanner.formats=CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode=AutoFocusMode.SAFE
        codeScanner.scanMode=ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled=true
        codeScanner.isFlashEnabled=false

        codeScanner.decodeCallback= DecodeCallback{
            runOnUiThread{
                Toast.makeText(this,"Scan text:${it.text}",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,TransferActivity::class.java)
                intent.putExtra(Constants.USER_KEY,it.toString())
                startActivity(intent)
                finish()
            }

        }
        codeScanner.errorCallback= ErrorCallback {
            runOnUiThread{
                Toast.makeText(this,"Scan text:${it.message}",Toast.LENGTH_SHORT).show()
            }
        }

        codeScanner.startPreview()

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.startPreview()
        super.onPause()
    }

    fun checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_REQUEST)
        }
        else
        {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==MY_CAMERA_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            codeScanner.startPreview()
        }
        else
        {
            Toast.makeText(this,"Can not scan",Toast.LENGTH_SHORT).show()
        }
    }



}