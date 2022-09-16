package com.ivan.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.chatHistoryTitle.Data
import com.ivan.letstalk.ui.MyALKChatActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChatHistoryTitleAdapter(var chatHistoryTitle: List<Data>, private val click: onClickListener) :
    RecyclerView.Adapter<ChatHistoryTitleAdapter.ChatHistoryTitleVH>() {
    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryTitleVH {
        context  = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.complete_chat_list_row, parent, false)
        return ChatHistoryTitleVH(view)
    }

    override fun onBindViewHolder(holder: ChatHistoryTitleVH, position: Int) {
        val chatHistory = chatHistoryTitle[position]
        val date = chatHistory.start_date
        var str1 = ""
        if (date.isNotEmpty()) {
            str1 = date
        }
        var splitDate = ""
        if (str1.isNotEmpty()) {
            splitDate = str1.split("\\s".toRegex())[0]
            Log.d("splitDate", splitDate)
        }
        var str2 = ""
        if (date.isNotEmpty()) {
            str2 = date
        }
        var splitTime = ""
        if (str2.isNotEmpty()) {
            splitTime = str2.split("\\s".toRegex())[1]
            Log.d("splitTime", splitTime)
        }

        var formattedTime = ""
        try {
            val time = splitTime
            val sdf = SimpleDateFormat("hh:mm:ss")
            val dt: Date = sdf.parse(time)
            val sdfs = SimpleDateFormat("hh:mm a")
            formattedTime = sdfs.format(dt)
            Log.v("parseTime", formattedTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        /*val dt = splitTime
        var sdf = SimpleDateFormat("hh:mm:ss")
        val time1 = sdf.format(dt)
        sdf = SimpleDateFormat("hh:mm aa")
        val newTimeString = time1?.let { sdf.format(it) }
        println(newTimeString)
        Log.d("newTimeString",newTimeString.toString())*/

        /*val dt = Date(date1)
        val sdf = SimpleDateFormat("hh:mm aa")
        val time1 = sdf.format(dt)*/

        /*val strDate = date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val mdate = dateFormat.parse(strDate)
        println(mdate)
        if (mdate != null) {
            Log.d("mdate", mdate.toString())
        }*/

        val mDate = splitDate
        var spf = SimpleDateFormat("yyyy-MM-dd")
        val newDate = spf.parse(mDate)
        spf = SimpleDateFormat("dd MMM yy")
        val newDateString = newDate?.let { spf.format(it) }
        println(newDateString)
        Log.d("newDateString",newDateString.toString())
        // holder.tvDate.text = newDateString
        if (chatHistory != null) {
            holder.tvChatDate.text = newDateString.plus(" ").plus("at").plus(" ").plus(formattedTime)
        }
        holder.rrChatHistoryRootView.setOnClickListener{
            click.onClick(position)
            // (context as MyALKChatActivity).chatHistoryDialog()
        }
    }

    override fun getItemCount(): Int {
        return chatHistoryTitle.size
    }

    inner class ChatHistoryTitleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvChatDate: TextView
        var rrChatHistoryRootView: RelativeLayout

        init {
            tvChatDate = itemView.findViewById(R.id.tv_chat_date)
            rrChatHistoryRootView = itemView.findViewById(R.id.rr_chat_history_root_view)
        }
    }

    companion object {
        private const val TAG = "ChatHistoryTitleAdapter"
    }

    interface onClickListener{
        fun onClick(pos:Int)
    }
}