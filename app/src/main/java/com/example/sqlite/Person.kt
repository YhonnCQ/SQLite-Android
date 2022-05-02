package com.example.sqlite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Long = -1,
    val name: String,
    val age: Int
): Parcelable