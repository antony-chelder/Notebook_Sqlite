package com.example.sqldatabasekotlin

import android.annotation.TargetApi
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqldatabasekotlin.db.MyAdapter
import com.example.sqldatabasekotlin.db.MyDbManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var sound: SoundPool? = null
    private var soundclick = 0
    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)
    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createSoundPool()
        loadSounds()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.OpenDb()
        FillAdapter("")
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.CloseDb()
    }

    fun OnClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)

    }
    fun init(){
        rcView.layoutManager = LinearLayoutManager(this)
        val swaphelper = getSwapManager()
        swaphelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter
    }
    protected fun createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool()
        } else {
            createOldSoundPool()
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun createNewSoundPool() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        sound = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .build()
    }

    protected fun createOldSoundPool() {
        sound = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
    }

    private fun loadSounds() {
        soundclick = sound!!.load(this, R.raw.poof, 1)
    }

    fun FillAdapter(text:String){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.ReadDbData(text)
            list.reverse()
            myAdapter.UpdateAdapter(list)


            if(list.size>0){
                tv_elements.visibility = View.GONE
            }else{
                tv_elements.visibility = View.VISIBLE

            }

        }

    }
    private fun initSearchView(){
        searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                FillAdapter(newText!!)
                return true
            }
        })
    }
   private fun getSwapManager(): ItemTouchHelper{
       return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
           0,
           ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
       ) {
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ): Boolean {
               return false
           }

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               myAdapter.DeletefromAdapter(viewHolder.adapterPosition, myDbManager)
               sound!!.play(soundclick, 1f, 1f, 1, 0, 1f)
               FillAdapter("")
           }
       })
   }

}