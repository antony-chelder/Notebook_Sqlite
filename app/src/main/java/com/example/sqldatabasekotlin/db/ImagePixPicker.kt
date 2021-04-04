package com.example.sqldatabasekotlin.db

import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix


object ImagePixPicker {
    const val REQUEST_CODE_GET_IMAGE = 54
    fun getImages(context:AppCompatActivity,imagecounter:Int,rCode : Int){
        val options: Options = Options.init()
            .setRequestCode(rCode) //Request code for activity results
            .setCount(imagecounter) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setMode(Options.Mode.Picture)
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
            .setPath("/pix/images") //Custom Path For media Storage


        Pix.start(context, options)
    }
}