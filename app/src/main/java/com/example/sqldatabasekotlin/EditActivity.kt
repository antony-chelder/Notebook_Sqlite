package com.example.sqldatabasekotlin

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqldatabasekotlin.db.ImageManager
import com.example.sqldatabasekotlin.db.ImagePixPicker
import com.example.sqldatabasekotlin.db.MyDbManager
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import kotlinx.android.synthetic.main.edit_activity.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val myDbManager = MyDbManager(this)
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
        if (resultCode == RESULT_OK && requestCode == ImagePixPicker.REQUEST_CODE_GET_IMAGE) {
            if (data != null) {
                val returnValue: ArrayList<String> =
                    data.getStringArrayListExtra(Pix.IMAGE_RESULTS) as ArrayList<String>
                if (returnValue.size == 1)
                     tempImageUri = returnValue[0].toString()
                     val imagefile = File(tempImageUri)
                     mainImage.setImageURI(Uri.fromFile(imagefile))


            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePixPicker.getImages(this, 1, ImagePixPicker.REQUEST_CODE_GET_IMAGE)
                } else {
                    Toast.makeText(

                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return

            }
        }
    }

    fun onClickAddImage(view: View) {
        ImageLayout.visibility = View.VISIBLE
        fbPic.visibility = View.GONE


    }

    fun onClickEditImage(view: View) {
        ImagePixPicker.getImages(this, 1, ImagePixPicker.REQUEST_CODE_GET_IMAGE)
        /*
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
        */
         */


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


        if (isEditState) {
            if (myTitle.isNotEmpty() && myDesc.isNotEmpty()) {
                myDbManager.UpdateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
                finish()
            } else {
                Toast.makeText(this, R.string.forget_fill, Toast.LENGTH_LONG).show()

            }

        } else {
            if (myTitle.isNotEmpty() && myDesc.isNotEmpty()) {
                myDbManager.InsertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
                finish()

            } else {
                Toast.makeText(this, R.string.forget_fill, Toast.LENGTH_LONG).show()
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

    fun getMyIntents() {
        b_edit_main.visibility = View.GONE

        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstans.MY_TITLE_KEY) != null) {
                fbPic.visibility = View.GONE
                edTtitle.setText(i.getStringExtra(MyIntentConstans.MY_TITLE_KEY))
                isEditState = true
                edTtitle.isEnabled = false
                edDesc.isEnabled = false




                b_edit_main.visibility = View.VISIBLE
                edDesc.setText(i.getStringExtra(MyIntentConstans.MY_DESC_KEY))
                id = i.getIntExtra(MyIntentConstans.I_ID, 0)

                if (i.getStringExtra(MyIntentConstans.MY_URI_KEY) != "empty") {
                    tempImageUri = i.getStringExtra(MyIntentConstans.MY_URI_KEY)!!
                    ImageLayout.visibility = View.VISIBLE
                    imButtonEdit.visibility = View.GONE
                    ImButtonDelete.visibility = View.GONE
                    mainImage.setImageURI(Uri.parse(tempImageUri))

                }


            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formaterTime = SimpleDateFormat("dd.MM.yy   kk:mm", Locale.getDefault())
        return formaterTime.format(time)
    }

    fun OnClickBack(view: View) {
        finish()
    }


}