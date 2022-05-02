package com.example.sqlite

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.sqlite.PersonReaderContract.PersonEntry

class PersonDao(private val dbHelper: PersonReaderDbHelper) {

    fun insertPerson(person: Person) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PersonEntry.COLUMN_NAME_NAME, person.name)
            put(PersonEntry.COLUMN_NAME_AGE, person.age)
        }
        db.insert(PersonEntry.TABLE_NAME, null, values)
        db.close()
    }


    fun getPerson(): List<Person> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PersonEntry.TABLE_NAME,
            null,
            null,
            null,
            null, null,
            null
        )
        val persons = mutableListOf<Person>()
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val itemName = getString(getColumnIndexOrThrow(PersonEntry.COLUMN_NAME_NAME))
                val itemAge = getInt(getColumnIndexOrThrow(PersonEntry.COLUMN_NAME_AGE))
                persons.add(Person(itemId, itemName, itemAge))
            }
        }
        cursor.close()
        db.close()
        return persons
    }

    fun updatePerson(person: Person) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PersonEntry.COLUMN_NAME_NAME, person.name)
            put(PersonEntry.COLUMN_NAME_AGE, person.age)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(person.id.toString())
        db.update(
            PersonEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs)
        db.close()
    }

    fun deletePerson(person: Person) {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(person.id.toString())
        db.delete(PersonEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}