package com.example.sqldatabasekotlin.db

import android.provider.BaseColumns

object MyDbNameClass  {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_IMAGE_URI = "image"
    const val COLUMN_TIME = "time"
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "MyLessonDb.db"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ("+
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$COLUMN_NAME_TITLE TEXT,$COLUMN_NAME_CONTENT TEXT,$COLUMN_IMAGE_URI TEXT,$COLUMN_TIME TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}