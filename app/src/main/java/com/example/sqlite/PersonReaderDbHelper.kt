package com.example.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.sqlite.PersonReaderContract.PersonEntry

class PersonReaderDbHelper(context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
){
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Person.db"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${PersonEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${PersonEntry.COLUMN_NAME_NAME} TEXT," +
                    "${PersonEntry.COLUMN_NAME_AGE} INTEGER)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PersonEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}