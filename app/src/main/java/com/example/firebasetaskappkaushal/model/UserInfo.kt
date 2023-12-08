package com.example.firebasetaskappkaushal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val age: Int,
    val classRoom: String,
    val image: String?
): Parcelable {
    constructor():this("","","","",0,"", null)
}