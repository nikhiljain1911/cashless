package com.example.cashless.Controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.cashless.Constants
import com.example.cashless.R
import com.example.cashless.Transactions.LatestTransactionsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth=Firebase.auth

        val entities=arrayOf("Student","Merchant")
        val adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,entities)

        spinner2.adapter=adapter



        spinner2.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,id: Long) {
                println("yes")
                println(spinner2.getSelectedItem().toString())
            }


        }

    }

    fun loginRegisterBtnClicked(view: View)
    {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }



    public override fun onStart() {
        super.onStart()

        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)
    }

    public fun loginLoginBtnClicked(view: View)
    {
        //enableSpinner(true)
        hideKeyboard()
        auth.signInWithEmailAndPassword(loginEmailText.text.toString(),loginPasswordText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    // enableSpinner(false)
                    val user = auth.currentUser
                    updateUI(user)
                    println("login successful")
                    val intent=Intent(this, LatestTransactionsActivity::class.java)
                    //intent.putExtra(Constants.USER_ENTITY,spinner2.selectedItem.toString())
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }

                else
                {
                    println("login failed")
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)

                }
            }

    }

    fun updateUI(currentUser: FirebaseUser?)
    {
        println("nik2")
        println(currentUser?.displayName)
    }

    fun hideKeyboard()
    {
        val inputManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText)
        {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)

        }
    }

}