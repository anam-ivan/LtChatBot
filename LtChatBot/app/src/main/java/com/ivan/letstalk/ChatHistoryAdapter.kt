
package com.ivan.letstalk

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.model.chatHistory.Conversation
import org.json.JSONException


class ChatHistoryAdapter(applicationContext: Context, private val inflater: LayoutInflater, var chatConversation: List<Conversation>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context = applicationContext
    // private val FADE_DURATION = 1500

    private inner class SentMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvSentMsg: TextView
        var tvTimeStamp: TextView

        init {
            tvSentMsg = itemView.findViewById(R.id.tv_sent)
            tvTimeStamp = itemView.findViewById(R.id.tv_time_stamp)
        }
    }

    private inner class ReceivedMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvReceivedMsg: TextView
        var tvReceiveTimeStamp: TextView

        init {
            tvReceivedMsg = itemView.findViewById(R.id.tv_received_msg)
            tvReceiveTimeStamp = itemView.findViewById(R.id.tv_receive_time_stamp)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = chatConversation[position]
        // Log.d("check_message", "message_list: ${Gson().toJson(message)}")
        try {
            return if (message.sender == "chatbot") {
                TYPE_MESSAGE_RECEIVED
            } else {
                TYPE_MESSAGE_SENT
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            TYPE_MESSAGE_SENT -> {
                view = inflater.inflate(R.layout.chat_send_message_item, parent, false)
                return SentMessageHolder(view)
            }
            TYPE_MESSAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.chat_receive_item_history, parent, false)
                return ReceivedMessageHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
        /* view = inflater.inflate(R.layout.chat_send_message_item, parent, false)
         return SentMessageHolder(view)*/
    }

    override fun getItemCount(): Int {
        return chatConversation.size
    }

    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        private const val TYPE_MESSAGE_SENT = 0
        private const val TYPE_MESSAGE_RECEIVED = 1
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatConversation[position]
            if (message.sender == "chatbot") {
                val messageHolder = holder as ReceivedMessageHolder
                Log.d("sent_message", message.toString())
                // setFadeAnimation(messageHolder.itemView)
                messageHolder.tvReceivedMsg.text = Html.fromHtml(message.text)
                val time = message.timestamp
                var str1 = ""
                if (time.isNotEmpty()) {
                    str1 = time
                }
                var splitTime = ""
                if (str1.isNotEmpty()) {
                    splitTime = str1.split("\\s".toRegex())[1]
                    Log.d("date", splitTime)
                }
                messageHolder.tvReceiveTimeStamp.visibility = View.VISIBLE
                messageHolder.tvReceiveTimeStamp.text = splitTime
            } else if (message.sender == "user") {
                val messageHolder = holder as SentMessageHolder
                // setFadeAnimation(messageHolder.itemView)
                messageHolder.tvSentMsg.text = Html.fromHtml(message.text)
                messageHolder.tvTimeStamp.visibility = View.VISIBLE
                val time = message.timestamp
                var str1 = ""
                if (time.isNotEmpty()) {
                    str1 = time
                }
                var splitTime = ""
                if (str1.isNotEmpty()) {
                    splitTime = str1.split("\\s".toRegex())[1]
                    Log.d("date", splitTime)
                }
                messageHolder.tvTimeStamp.text = splitTime
            }
    }
    /*private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }*/
}
