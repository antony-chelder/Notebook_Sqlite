package com.example.sqldatabasekotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.sqldatabasekotlin.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(val context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db:SQLiteDatabase? = null

    fun OpenDb(){
        db = myDbHelper.writableDatabase
    }
     suspend fun InsertToDb(title:String,content:String,image:String,time:String) = withContext(Dispatchers.IO){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_IMAGE_URI,image)
            put(MyDbNameClass.COLUMN_TIME,time)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null,values)

    }

     suspend fun UpdateItem(title:String,content:String,image:String,id:Int,time: String) = withContext(Dispatchers.IO){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_IMAGE_URI,image)
            put(MyDbNameClass.COLUMN_TIME,time)
        }
        db?.update(MyDbNameClass.TABLE_NAME,values,selection,null)

    }
   suspend fun ReadDbData(searchtext:String):ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like?"

        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,selection, arrayOf("%$searchtext%"),null,null,null)


            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
                val datadisc = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_IMAGE_URI))
                val dataTime = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_TIME))
                val dataID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val item = ListItem()
                item.title = dataTitle
                item.id = dataID
                item.time = dataTime
                item.desc = datadisc
                item.imageUri = dataUri
                dataList.add(item)
            }
        cursor.close()

        return@withContext dataList
    }
    fun CloseDb(){
        myDbHelper.close()

    }
    fun DeletefromDb(id:String){
        val selection = BaseColumns._ID + "=$id"

        db?.delete(MyDbNameClass.TABLE_NAME,selection,null)

    }

}