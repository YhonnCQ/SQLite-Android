package com.example.sqlite

import android.provider.BaseColumns

object PersonReaderContract {
    object PersonEntry: BaseColumns {
        const val TABLE_NAME = "person"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_AGE = "age"
    }
}