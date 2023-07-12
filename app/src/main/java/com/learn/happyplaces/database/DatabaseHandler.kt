package com.learn.happyplaces.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.learn.happyplaces.models.HappyPlaceModel


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "HappyPlacesDatabase" // Database name
        private const val TABLE_HAPPY_PLACE = "HappyPlacesTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE " + TABLE_HAPPY_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(db)
    }

    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, happyPlace.image) // HappyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            happyPlace.description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, happyPlace.date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, happyPlace.location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, happyPlace.latitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude) // HappyPlaceModelClass LONGITUDE

        // Inserting Row
        val result = db.insert(TABLE_HAPPY_PLACE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return result
    }

    fun getHappyPlacesList(): ArrayList<HappyPlaceModel> {
        val happyPlaceList: ArrayList<HappyPlaceModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACE"
        val db = this.readableDatabase
        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getColumnIndex(KEY_ID)
                    val titleIndex = cursor.getColumnIndex(KEY_TITLE)
                    val imageIndex = cursor.getColumnIndex(KEY_IMAGE)
                    val descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION)
                    val dateIndex = cursor.getColumnIndex(KEY_DATE)
                    val locationIndex = cursor.getColumnIndex(KEY_LOCATION)
                    val latitudeIndex = cursor.getColumnIndex(KEY_LATITUDE)
                    val longitudeIndex = cursor.getColumnIndex(KEY_LONGITUDE)

                    if (titleIndex != -1 && imageIndex != -1 && descriptionIndex != -1 &&
                        dateIndex != -1 && locationIndex != -1 && latitudeIndex != -1 && longitudeIndex != -1) {
                        val place = HappyPlaceModel(
                            cursor.getInt(id),
                            cursor.getString(titleIndex),
                            cursor.getString(imageIndex),
                            cursor.getString(descriptionIndex),
                            cursor.getString(dateIndex),
                            cursor.getString(locationIndex),
                            cursor.getDouble(latitudeIndex),
                            cursor.getDouble(longitudeIndex)
                        )
                        happyPlaceList.add(place)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }

    fun updateHappyPlace(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title) // HappyPlaceModelClass TITLE
        contentValues.put(KEY_IMAGE, happyPlace.image) // HappyPlaceModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            happyPlace.description
        ) // HappyPlaceModelClass DESCRIPTION
        contentValues.put(KEY_DATE, happyPlace.date) // HappyPlaceModelClass DATE
        contentValues.put(KEY_LOCATION, happyPlace.location) // HappyPlaceModelClass LOCATION
        contentValues.put(KEY_LATITUDE, happyPlace.latitude) // HappyPlaceModelClass LATITUDE
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude) // HappyPlaceModelClass LONGITUDE

        val success =
            db.update(TABLE_HAPPY_PLACE, contentValues, KEY_ID + "=" + happyPlace.id, null)

        db.close() // Closing database connection
        return success
    }

    fun deleteHappyPlace(happyPlace: HappyPlaceModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_HAPPY_PLACE, KEY_ID + "=" + happyPlace.id, null)
        db.close()
        return success
    }
}