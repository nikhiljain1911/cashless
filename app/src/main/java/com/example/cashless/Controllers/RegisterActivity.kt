package com.example.cashless.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.cashless.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
       //val spin=findViewById(R.id.spinner1) as Spinner
        val entities=arrayOf("Student","Merchant")
        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,entities)

                spinner1.adapter=adapter



        spinner1.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
             override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                println("yes")
                println(spinner1.getSelectedItem().toString())
            }


        }

        registerContinueBtn.setOnClickListener {
            if(spinner1.getSelectedItem().toString()=="Student")
            {
                startActivity(Intent(this,
                    StudentRegisterActivity::class.java))
            }
//            else if(spinner1.getSelectedItem().toString()=="Merchant")
            else
            {
                startActivity(Intent(this,
                    MerchantRegisterActivity::class.java))
            }
        }
    }
}