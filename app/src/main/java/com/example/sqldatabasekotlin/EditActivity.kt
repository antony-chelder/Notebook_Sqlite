package com.example.sqldatabasekotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqldatabasekotlin.db.MyDbManager
import kotlinx.android.synthetic.main.edit_activity.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val myDbManager = MyDbManager(this)
    val IMAGE_REQUEST_CODE = 54
    var tempImageUri = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyIntents()
    }
    override fun onResume() {
        super.onResume()
        myDbManager.OpenDb()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.CloseDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE ){
            mainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()


        }
    }
    fun onClickAddImage(view: View) {
        ImageLayout.visibility = View.VISIBLE
        fbPic.visibility = View.GONE


    }

    fun onClickEditImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, IMAGE_REQUEST_CODE)


        /*val options = Options.init()
            .setRequestCode(IMAGE_REQUEST_CODE) //Request code for activity results
            .setCount(1) //Number of images to restict selection count
            .setFrontfacing(false) //Front Facing camera on star//Pre selected Image Urls
            .setSpanCount(4) //Span count for gallery min 1 & max 5
            .setExcludeVideos(true) //Option to exclude videos
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images");


        Pix.start(this, options.setRequestCode(IMAGE_REQUEST_CODE))*/




    }
    fun onClickDeleteImage(view: View) {
         tempImageUri = "empty"
        mainImage.setImageResource(R.drawable.ic_image_gallery)
        ImageLayout.visibility = View.GONE
        fbPic.visibility = View.VISIBLE

    }
    fun onClickSave(view: View) {
        val myTitle = edTtitle.text.toString()
        val myDesc = edDesc.text.toString()


        if(isEditState){
            if (myTitle.isNotEmpty() && myDesc.isNotEmpty()) {
                myDbManager.UpdateItem(myTitle, myDesc, tempImageUri, id,getCurrentTime())
                finish()
            } else{
                Toast.makeText(this,R.string.forget_fill,Toast.LENGTH_LONG).show()

            }

        }else{
        if(myTitle.isNotEmpty() && myDesc.isNotEmpty()) {
            myDbManager.InsertToDb(myTitle, myDesc, tempImageUri,getCurrentTime())
            finish()

        }
            else{
                Toast.makeText(this,R.string.forget_fill,Toast.LENGTH_LONG).show()
            }



        }

    }
    fun onClickEditEnable(view: View) {
        edTtitle.isEnabled = true
        edDesc.isEnabled = true
        back_button.visibility = View.GONE
        imButtonEdit.visibility = View.VISIBLE
        ImButtonDelete.visibility = View.VISIBLE
        fbPic.visibility = View.VISIBLE


        b_edit_main.visibility = View.GONE

    }
    fun getMyIntents(){
        b_edit_main.visibility = View.GONE

        val i = intent
        if(i!=null){
           if (i.getStringExtra(MyIntentConstans.MY_TITLE_KEY)!= null){
               fbPic.visibility = View.GONE
               edTtitle.setText(i.getStringExtra(MyIntentConstans.MY_TITLE_KEY))
               isEditState = true
               edTtitle.isEnabled = false
               edDesc.isEnabled = false


               

               b_edit_main.visibility = View.VISIBLE
               edDesc.setText(i.getStringExtra(MyIntentConstans.MY_DESC_KEY))
               id  = i.getIntExtra(MyIntentConstans.I_ID,0)

               if( i.getStringExtra(MyIntentConstans.MY_URI_KEY) !="empty"){
                   tempImageUri = i.getStringExtra(MyIntentConstans.MY_URI_KEY)!!
                   ImageLayout.visibility = View.VISIBLE
                   imButtonEdit.visibility = View.GONE
                   ImButtonDelete.visibility = View.GONE
                   mainImage.setImageURI(Uri.parse(tempImageUri))

               }


           }
        }
    }
    private fun getCurrentTime():String{
        val time = Calendar.getInstance().time
        val formaterTime = SimpleDateFormat("dd.MM.yy   kk:mm",Locale.getDefault())
        return formaterTime.format(time)
    }

    fun OnClickBack(view: View) {
        finish()
    }


}