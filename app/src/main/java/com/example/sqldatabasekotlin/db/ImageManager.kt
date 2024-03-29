package com.example.sqldatabasekotlin.db

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.File

object ImageManager {
    fun getImageSize(uri:String):List<Int>{
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uri,options)
        return if(ImageRotatiton(uri)==90)
            listOf(options.outHeight,options.outWidth)
        else listOf(options.outWidth,options.outHeight)



    }

  private fun ImageRotatiton(uri:String) : Int{
        val rotation: Int
        val imagefile = File(uri)
        val exif = ExifInterface(imagefile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        rotation = if(orientation == ExifInterface.ORIENTATION_ROTATE_90 ||  orientation == ExifInterface.ORIENTATION_ROTATE_270 ){
            90

        }else{
            0
        }
        return rotation

    }
}