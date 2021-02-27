package com.example.sqldatabasekotlin.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqldatabasekotlin.EditActivity
import com.example.sqldatabasekotlin.ListItem
import com.example.sqldatabasekotlin.MyIntentConstans
import com.example.sqldatabasekotlin.R
import kotlinx.android.synthetic.main.rc_item.view.*
import java.util.ArrayList
import java.util.zip.Inflater

class MyAdapter(listMain:ArrayList<ListItem>,contextM:Context) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM
    class MyHolder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTime : TextView = itemView.findViewById(R.id.tv_time)
        val context = contextV
        fun SetData(item:ListItem){
            tvTitle.text = item.title
            tvTime.text = item.time

            itemView.setOnClickListener{
                val intent = Intent(context,EditActivity::class.java).apply {
                    putExtra(MyIntentConstans.MY_TITLE_KEY,item.title)
                    putExtra(MyIntentConstans.MY_DESC_KEY,item.desc)
                    putExtra(MyIntentConstans.MY_URI_KEY,item.imageUri)
                    putExtra(MyIntentConstans.I_ID,item.id)

                }
                context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item,parent,false),context)

    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.SetData(listArray.get(position))

    }

    override fun getItemCount(): Int {
       return listArray.size

    }
    fun UpdateAdapter(listItems:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()

    }
    fun DeletefromAdapter(pos:Int,dbManager: MyDbManager){
        dbManager.DeletefromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(pos)



    }
}