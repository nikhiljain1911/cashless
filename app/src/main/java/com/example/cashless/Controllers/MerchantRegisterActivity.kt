package com.example.cashless.Controllers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.cashless.Constants
import com.example.cashless.R
import com.example.cashless.DataService.merchantData
import com.example.cashless.Transactions.LatestTransactionsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_merchant_register.*
import kotlinx.android.synthetic.main.activity_merchant_register.circularImage
import kotlinx.android.synthetic.main.activity_merchant_register.haveMerchantAccount
import kotlinx.android.synthetic.main.activity_merchant_register.selectPhotoBtn
import java.util.*

class MerchantRegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var selectedphotoUri: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchant_register)
        auth = Firebase.auth

        registerMerchantRegisterBtn.setOnClickListener {
            createUser()
        }

        haveMerchantAccount.setOnClickListener{
            Log.d("MainActivity","Try to show login activity")
            startActivity(
                Intent(this,
                    LoginActivity::class.java)
            )

        }


    }

    fun selectPhotoClicked(view: View)
    {
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null)
        {
            Log.d("MainActivity","Photo was selected")
            selectedphotoUri=data.data
            println("yes")
            println(selectedphotoUri)
            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,selectedphotoUri)
            circularImage.setImageBitmap(bitmap)
            selectPhotoBtn.alpha=0f
//            val bitmapDrawable=BitmapDrawable(bitmap)
//            selectPhotoBtn.setBackgroundDrawable(bitmapDrawable)
            selectPhotoBtn.text=""
        }

    }



    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?)
    {

    }

    public fun createUser()
    {
        val email=registerMerchantEmail.text.toString()
        val password=registerMerchantPassword.text.toString()

        if(email.isEmpty())
        {
            registerMerchantEmail.error="Please enter email"
            registerMerchantEmail.requestFocus()


        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            registerMerchantEmail.error="Please enter valid email"
            registerMerchantEmail.requestFocus()

        }

        else if(password.isEmpty())
        {
            registerMerchantPassword.error="Please enter Password"
            registerMerchantPassword.requestFocus()


        }

        else
        {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    println("hello nick1")

                    if (task.isSuccessful) {
                        saveUser()

                    } else {

                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }
    }

    private fun saveUser()
    {
        val email=registerMerchantEmail.text.toString()
        val password=registerMerchantPassword.text.toString()
        val username=registerMerchantUsername.text.toString()
        val uid=auth.uid.toString()
        val shoplocation=registerShopLocation.text.toString()
        val user = auth.currentUser


        if(selectedphotoUri!=null)
        {

            val storage= FirebaseStorage.getInstance()
            val filename= UUID.randomUUID().toString()
            println(filename)
            val sref=storage.getReference("/merchant-images/$filename")


            sref.putFile(selectedphotoUri!!)
                .addOnSuccessListener {
                    Log.d("mainActivity","imageUploaded")
                    println("ohoo")
                    sref.downloadUrl.addOnSuccessListener {
                        val database = FirebaseDatabase.getInstance()
                        val mref = database.getReference("users-merchants")
                        val user1 =
                            merchantData(
                                uid,
                                username,
                                email,
                                password,
                                shoplocation,
                                it.toString()
                            )
                        mref.child(user?.uid.toString()).setValue(user1).addOnCompleteListener {
                            Toast.makeText(this, "data saved successfully", Toast.LENGTH_LONG)
                                .show()


                            val intent= Intent(this, LatestTransactionsActivity::class.java)
                            //intent.putExtra(Constants.USER_ENTITY,"Merchant")
                            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                }


        }


    }
}