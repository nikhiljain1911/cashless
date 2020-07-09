package com.example.cashless.DataService

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class merchantData(val uid:String,val username: String, val email: String, val password: String, val shoplocation:String,val profileImageUrl: String):Parcelable {

    constructor():this ("","","","","","")
}