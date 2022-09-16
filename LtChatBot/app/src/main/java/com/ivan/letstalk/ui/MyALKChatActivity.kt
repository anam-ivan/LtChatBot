package com.ivan.letstalk.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.ivan.letstalk.ChatHistoryAdapter
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.ChatHistoryTitleAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityMyAlkchatBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.chatHistory.Conversation
import com.ivan.letstalk.model.chatHistoryTitle.ChatHistoryTitlePayload
import com.ivan.letstalk.model.chatHistoryTitle.ChatIdBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyALKChatActivity : AppCompatActivity() {
    private var transparentDialogLoader = TransparentDialogLoader()
    private var chatDate = ""
    lateinit var chatConversation: List<Conversation>
    private var chatId = ""
    private lateinit var dialog: BottomSheetDialog
    private lateinit var chatHistoryTitleAdapter: ChatHistoryTitleAdapter
    private lateinit var chatHistoryAdapter: ChatHistoryAdapter
    private var userId = ""
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityMyAlkchatBinding
    private lateinit var tvArchived: AppCompatTextView
    private lateinit var tvCompleted: AppCompatTextView
    private lateinit var rrFirst: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_my_alkchat)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_alkchat)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        tvArchived = findViewById(R.id.tv_archived)
        tvCompleted = findViewById(R.id.tv_completed)
        tvCompleted.setTextColor(ContextCompat.getColor(this, R.color.blue))
        tvCompleted.paint?.isUnderlineText = true
        /*rrFirst = findViewById(R.id.rr_first)
        rrFirst.setOnClickListener{
            val singleChatFragment =
                SingleChatFragment()
            singleChatFragment.show(supportFragmentManager, "ddd")
        }*/
        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        setupViewModel()
        setUpChatHistoryTitle()
        // setUpChatHistory()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setUpChatHistoryTitle() {
        val body = ChatHistoryTitlePayload.ChatHistoryTitle(
            limit = 50,
            page_no = 1,
            perpage = 1,
            filter_date = "",
            user_id = userId,
            status = "ACTIVE"
        )
        viewModel.chatHistoryTitle(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            Log.d("TAG", "chatTitleList: ${Gson().toJson(it.body()!!.data)}")
                            chatHistoryTitleAdapter = ChatHistoryTitleAdapter(it.body()!!.data,
                                object : ChatHistoryTitleAdapter.onClickListener {
                                    override fun onClick(pos: Int) {
                                        it.body()!!.data[pos]._id.let {
                                            Log.d("chatId", it)
                                            chatId = it
                                            setUpChatHistory(chatId)
                                        }
                                    }

                                })
                            binding.rvMyChats.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.rvMyChats.itemAnimator = DefaultItemAnimator()
                            binding.rvMyChats.adapter = chatHistoryTitleAdapter
                        }
                    }
                    Status.ERROR -> {
                        Log.d("errorChat", it.message.toString())
                        // showErrorMsg(it.message.toString(), binding.root)
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        })
    }

    private fun setUpChatHistory(chatId: String) {
        val body = ChatIdBodies.ChatId(
            _id = chatId
        )
        viewModel.chatHistory(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { it ->
                            Log.d(
                                "TAG",
                                "chatList: ${Gson().toJson(it.body()!!.data.conversation)}"
                            )
                            if (it.body() != null) {
                                if (it.body()!!.status == "success") {
                                    if (it.body()!!.data != null) {
                                        //chatDate = it.body()!!.data.chatInfo!!.start_date
                                        val date = it.body()!!.data.chatInfo!!.start_date
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
                                        val mDate = splitDate
                                        var spf = SimpleDateFormat("yyyy-MM-dd")
                                        val newDate = spf.parse(mDate)
                                        spf = SimpleDateFormat("dd MMM yy")
                                        val newDateString = newDate?.let { spf.format(it) }
                                        println(newDateString)
                                        Log.d("newDateString",newDateString.toString())
                                        chatDate = newDateString.plus(" ").plus("at").plus(" ").plus(formattedTime)
                                    }
                                    chatConversation = it.body()!!.data.conversation
                                    if (chatConversation.isNotEmpty()) {
                                        chatHistoryDialog(chatConversation, chatDate)
                                    }
                                }
                            }
                            /*if (it.body()!!.status == "success") {
                                if (it.body()!!.data != null) {
                                    chatDate = it.body()!!.data.chatInfo!!.start_date
                                }
                                chatConversation = it.body()!!.data.conversation
                                if (chatConversation.isNotEmpty()) {
                                    chatHistoryDialog(chatConversation, chatDate)
                                }
                            }*/
                            // chatHistoryDialog(chatConversation)
                        }
                    }
                    Status.ERROR -> {
                        Log.d("errorChatHistory", it.message.toString())
                        // showErrorMsg(it.message.toString(), binding.root)
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }

    private fun chatHistoryDialog(chatConversation: List<Conversation>, chatDate: String) {
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // dialog.behavior.maxHeight = 1000 // set max height when expanded in PIXEL
        dialog.behavior.peekHeight = 1100 // set default height when collapsed in PIXEL
        val view = layoutInflater.inflate(R.layout.chat_history_dialog, null)
        val rvChatHistory = view.findViewById<RecyclerView>(R.id.rv_chat_history)
        val ivClose = view.findViewById<ImageView>(R.id.iv_close)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        tvDate.text = chatDate
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
        chatHistoryAdapter = ChatHistoryAdapter(this, layoutInflater, chatConversation)
        rvChatHistory.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvChatHistory.itemAnimator = DefaultItemAnimator()
        rvChatHistory.adapter = chatHistoryAdapter
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}