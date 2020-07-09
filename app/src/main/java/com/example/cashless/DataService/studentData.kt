package com.example.cashless.DataService

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class studentData (val uid:String,val username: String,val rollNo:String, val email: String, val password: String, val profileImageUrl: String):Parcelable{

    constructor() :this("","","","","","")
}