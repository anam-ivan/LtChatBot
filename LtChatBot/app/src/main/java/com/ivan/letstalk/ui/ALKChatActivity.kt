package com.ivan.letstalk.ui
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.ChatAdapter
import com.ivan.letstalk.databinding.ActivityAlkchatBinding
import com.ivan.letstalk.helper.SessionManager
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject


class ALKChatActivity : AppCompatActivity() {
    private var isOthersSelected: Boolean = false
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var mTime: Long = 10000
    lateinit var binding: ActivityAlkchatBinding
    private var token = ""
    private lateinit var dialog: Dialog
    private lateinit var sessionManager: SessionManager
    val gson: Gson = Gson()
    private var isConnect = false
    private var messageAdapter: ChatAdapter? = null
    private val FADE_DURATION = 1500
    private var existingSideEffectsList = arrayOf(
        "Abdominal Pain",
        "Constipation",
        "Dyspepsia",
        "Dysphagia",
        "Electrocardiogram QT prlonged",
        "Nausea",
        "Vomiting",
        "Vision Disorder",
        "Constipation",
        "Dyspepsia",
        "Nausea",
        "Abdominal Pain",
        "Constipation",
        "Dyspepsia",
        "Dysphagia",
        "Electrocardiogram QT prlonged",
        "Nausea",
        "Vomiting",
        "Vision Disorder",
        "Constipation",
        "Dyspepsia",
        "Nausea"
    )
    private var existingSideEffectsChipItems = ArrayList<String>()

    private var webSocket: WebSocket? = null

    // private val SERVER_PATH = "ws://59.160.194.50:60500"
    private val SERVER_PATH = "ws://184.171.249.242:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alkchat)
        sessionManager = SessionManager(this)
        token = sessionManager.fetchAuthToken().toString()
        Log.d("token", token)
        initExistingSideEffectsData()
        binding.rrMyChats.setOnClickListener {
            navigateToMyChats()
        }
        /*binding.btnBack.setOnClickListener {
            onBackPressed()
            // disconnectSocket()
            webSocket?.close(1,"CHAT_CLOSE")
            this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }*/

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setOnCancelListener(DialogInterface.OnCancelListener {
            binding.ivMenu.visibility = View.VISIBLE
            binding.ivCross.visibility = View.INVISIBLE
        })

        binding.ivMenu.setOnClickListener {
            showChatDialog()
            binding.ivMenu.visibility = View.INVISIBLE
            binding.ivCross.visibility = View.VISIBLE
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        if (binding.ivCross.visibility == View.VISIBLE) {
            binding.ivCross.setOnClickListener {
                // ivCross.visibility = View.INVISIBLE
                // ivMenu.visibility = View.VISIBLE
                dialog.dismiss()
            }
        }

        /*binding.btnSendMessage.setOnClickListener {
            initiateSocketConnection()
            joinChatWithToken()
            // emitUserMessage()
            Log.d("isConnect",isConnect().toString())
            messageAdapter = ChatAdapter(this,layoutInflater)
            binding.rvChat.adapter = messageAdapter
            binding.rvChat.layoutManager = LinearLayoutManager(this)
        }*/

        messageAdapter = ChatAdapter(this, layoutInflater)
        binding.rvChat.adapter?.setHasStableIds(true)
        binding.rvChat.adapter = messageAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        initiateSocketConnection()
        joinChatWithToken()
        binding.btnSendMessage.setOnClickListener {
            if (binding.edtSendMessage.text.toString().trim().isEmpty()) {
                showErrorMsg("Please enter a message", binding.root)
            } else {
                if (isOthersSelected) {
                    emitOtherMessage()
                } else {
                    emitUserMessage()
                }
            }
            /*if (isOthersSelected) {
                emitOtherMessage()
            }*/
        }
        // emitUserMessage()
        Log.d("isConnect", isConnect().toString())
        // Initializing the handler and the runnable
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable {
            /*Toast.makeText(
                applicationContext,
                "User inactive for ${mTime / 1000} secs!",
                Toast.LENGTH_SHORT
            ).show()*/
            Log.d("inactive","User inactive for ${mTime / 1000} secs!")
        }
        startHandler()
    }

    private fun initExistingSideEffectsData() {
        for (i in existingSideEffectsList.indices) {
            // cgExistingSideEffects = findViewById(R.id.chip_existing_side_effects)
            val entryChip2: Chip = getChip(existingSideEffectsList[i])
            entryChip2.id = i
        }
    }

    private fun getChip(text: String): Chip {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 50f,
            resources.displayMetrics
        ).toInt()
        chip.setChipBackgroundColorResource(R.color.divider_color)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        chip.isCloseIconVisible = false
        chip.isCheckedIconVisible = false
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.chat_answer_select)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
                chip.isChecked = true
                existingSideEffectsChipItems.add(chip.text.toString())
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.divider_color)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.black))
                chip.isChecked = false
                existingSideEffectsChipItems.remove(chip.text.toString())
            }
        }
        return chip
    }

    private fun navigateToMyChats() {
        val intent = Intent(this, MyALKChatActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun showChatDialog() {
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setGravity(Gravity.LEFT)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.window?.attributes!!.verticalMargin = 0.2F
        dialog.window?.attributes!!.horizontalMargin = 0.1F
        val params = this.window.attributes
        // this.setCanceledOnTouchOutside(true)
        // params.x = -100
        this.window.attributes = params
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.chat_dialog)
        dialog.show()
    }

    class SocketListener(_context: ALKChatActivity) : WebSocketListener() {
        var context: ALKChatActivity? = _context

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Handler(Looper.getMainLooper()).post {
                /*context!!.messageAdapter = ChatAdapter(context!!,context!!.layoutInflater)
                context!!.binding.rvChat.adapter = context!!.messageAdapter
                context!!.binding.rvChat.layoutManager = LinearLayoutManager(context)*/
                // isConnect = response.code == 101
                if (response.code == 101) {
                    context!!.isConnect = true
                    // context!!.emitUserMessage()
                }
                Log.v("response_code", response.code.toString())
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Handler(Looper.getMainLooper()).post {
                if (text.isNotEmpty()) {
                    Log.v("WSS", text)
                    val jsonObject = JSONObject(text)
                    // val dataJsonObject = jsonObject.getJSONObject("data")
                    /*if(dataJsonObject.has("data") && !dataJsonObject.isNull("data") ) {*/
                    val statusCode = jsonObject.getString("status_code")
                    if (statusCode.equals("200")) {
                        if (!jsonObject.isNull("data")) {
                            val dataJsonObject = jsonObject.getJSONObject("data")
                            Log.d("statusCode", statusCode.toString())
                            if (dataJsonObject.has("chatid")) {
                                val chatID = dataJsonObject.getString("chatid")
                                chatId = chatID
                                Log.v("chatID", chatId)
                            } else {
                                var stopOption = ""
                                Log.v("webSocketResponse", text)
                                if (dataJsonObject.has("response_text")) {
                                    val messageType = dataJsonObject.optString("message_type")
                                    Log.v("response_text", text)
                                    jsonObject.put("isSent", false)
                                    jsonObject.put("message_type", messageType)
                                    context!!.messageAdapter?.addItem(jsonObject)
                                    context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                    val option = dataJsonObject.getString("option")
                                    stopOption = option
                                    Log.d("option", option)
                                    if (option.isNotEmpty() && option.toString() == "next&2&") {
                                        context!! emitUserMessageOption ("next&2&")
                                        Log.v("health_options", text)
                                    }
                                    if (messageType.equals("patient_request")) {
                                        jsonObject.put("isSent", true)
                                        val mType = dataJsonObject.getString("message_type")
                                        jsonObject.put("message_type", mType)
                                        val nextOption = dataJsonObject.getString("option")
                                        Log.d("stopOption", stopOption)
                                        if (nextOption.isNotEmpty()) {
                                            context!! emitUserMessageOption (nextOption)
                                            Log.v("next_option", text)
                                        }
                                    }

                                    /*val responseId = dataJsonObject.getString("response_id")
                                    if (responseId == "24" || responseId == "25" || responseId == "26") {
                                        Log.d("chat close", "chat close")
                                        context!!.disableEditText(context!!.binding.edtSendMessage)
                                        context!!.binding.tvChatEnd.visibility = View.VISIBLE
                                        context!!setFadeAnimation(context!!.binding.tvChatEnd)
                                    }*/
                                    if (stopOption == "stop") {
                                        Log.d("chat close", "chat close")
                                        context!!.disableEditText(context!!.binding.edtSendMessage)
                                        context!!.binding.tvChatEnd.visibility = View.VISIBLE
                                        context!! setFadeAnimation (context!!.binding.tvChatEnd)
                                        // context!!.messageAdapter?.disableAllViews()
                                        /*context!!.disableEnableControls(
                                            false,
                                            context!!.binding.llRvChat
                                        )*/
                                    }
                                    /*if (messageType.equals("side_effects_list")) {
                                        jsonObject.put("isSent", true)
                                        val mType = dataJsonObject.getString("message_type")
                                        jsonObject.put("message_type", mType)
                                        // context!!.messageAdapter?.addItem(jsonObject)
                                        context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                        val nextOption = dataJsonObject.getString("option")
                                        if (nextOption.isNotEmpty()) {
                                            context!! emitUserMessageOption (nextOption)
                                            Log.v("next_option", text)
                                        }
                                    }*/
                                }
                            }
                        } else {
                            /*if (statusCode.equals("400")) {
                                val message = jsonObject.getString("message")
                                jsonObject.put("isSent", true)
                                jsonObject.put("message_type", "do_not_know")
                                context!!.messageAdapter?.addItem(jsonObject)
                                context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                Log.d("message", message)
                            }*/
                            if (jsonObject.getString("message_type").equals("not_understand")) {
                                val message = jsonObject.getString("message")
                                Log.d("mOption", "ff")
                                val jsonObject = JSONObject()
                                try {
                                    jsonObject.put("message", message)
                                    jsonObject.put("isSent", false)
                                    jsonObject.put("message_type", "not_understand")
                                    context!!.messageAdapter!!.addItem(jsonObject)
                                    context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
                /*val statusCode = jsonObject.getString("status_code")
                    if (statusCode.equals("200")) {
                        Log.d("statusCode", statusCode.toString())
                        if (dataJsonObject.has("chatid")) {
                            val chatID = dataJsonObject.getString("chatid")
                            chatId = chatID
                            Log.v("chatID", chatId)
                        } else {
                            Log.v("webSocketResponse", text)
                            if (dataJsonObject.has("response_text")) {
                                val messageType = dataJsonObject.optString("message_type")
                                Log.v("response_text", text)
                                jsonObject.put("isSent", false)
                                jsonObject.put("message_type",messageType)
                                context!!.messageAdapter?.addItem(jsonObject)
                                context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                val option = dataJsonObject.getString("option")
                                Log.d("option",option)
                                if (option.isNotEmpty() && option.toString() == "next&2&") {
                                    context!! emitUserMessageOption ("next&2&")
                                    Log.v("health_options", text)
                                }
                                if (messageType.equals("patient_request")) {
                                    jsonObject.put("isSent", true)
                                    val mType = dataJsonObject.getString("message_type")
                                    jsonObject.put("message_type", mType)
                                    val nextOption = dataJsonObject.getString("option")
                                    if (nextOption.isNotEmpty()) {
                                        context!! emitUserMessageOption (nextOption)
                                        Log.v("next_option", text)
                                    }
                                }
                                *//*if (messageType.equals("side_effects_list")) {
                                    jsonObject.put("isSent", true)
                                    val mType = dataJsonObject.getString("message_type")
                                    jsonObject.put("message_type", mType)
                                    // context!!.messageAdapter?.addItem(jsonObject)
                                    *//**//*context!!.binding.rvChat.smoothScrollToPosition(context!!.messageAdapter!!.itemCount - 1)
                                    val nextOption = dataJsonObject.getString("option")
                                    if (nextOption.isNotEmpty()) {
                                        context!! emitUserMessageOption (nextOption)
                                        Log.v("next_option", text)
                                    }*//**//*
                                }*//*
                            }
                        }
                    }*/
                // }
            }
        }
    }

    private fun initiateSocketConnection() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(SERVER_PATH)
            .build()
        val wsListener = SocketListener(this)
        webSocket = client.newWebSocket(request, wsListener)
    }

    private fun joinChatWithToken() {
        val jsonObject = JSONObject()
        try {
            /*jsonObject.put(
                "token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6dHJ1ZSwiZW1haWxfaWQiOiJhZG1pbnVzZXJAeW9wbWFpbC5jb20iLCJyb2xlX2lkIjoiMSIsIl9pZCI6IjYyY2Q2ZDBmMDFlMTY1YzE2NjY5MDcwMSIsImV4cCI6MTY2MTY2NjMyNCwibW9iaWxlIjoiOTEtMTIzNDU2Nzg5MCJ9.wZEmAC7LzLSx9X7Z42GtQQG4DpCZfApWfW61zpQOVd8"
            )*/
            jsonObject.put(
                "token",
                token
            )
            webSocket!!.send(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /*private fun emitUserMessage() {
        binding.btnSendMessage.setOnClickListener(View.OnClickListener { v: View? ->
            val jsonObject = JSONObject()
            try {
                jsonObject.put("user", binding.edtSendMessage.text.toString().trim())
                jsonObject.put("chatid", chatId)
                webSocket!!.send(jsonObject.toString())
                binding.edtSendMessage.setText("")
                hideSoftKeyboard(binding.edtSendMessage)
                jsonObject.put("isSent", true)
                jsonObject.put("message_type", "first_message")
                messageAdapter!!.addItem(jsonObject)
                binding.rvChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
    }*/

    private fun emitUserMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", binding.edtSendMessage.text.toString().trim())
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            binding.edtSendMessage.setText("")
            // disableEditText(binding.edtSendMessage)
            hideSoftKeyboard(binding.edtSendMessage)
            jsonObject.put("isSent", true)
            jsonObject.put("message_type", "first_message")
            messageAdapter!!.addItem(jsonObject)
            binding.rvChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun emitOtherMessage() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", binding.edtSendMessage.text.toString().trim())
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            binding.edtSendMessage.setText("")
            // disableEditText(binding.edtSendMessage)
            hideSoftKeyboard(binding.edtSendMessage)
            /*jsonObject.put("isSent", true)
            jsonObject.put("message_type", "first_message")
            messageAdapter!!.addItem(jsonObject)
            binding.rvChat.smoothScrollToPosition(messageAdapter!!.itemCount - 1)*/
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private infix fun emitUserMessageOption(option: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", option)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("next", Gson().toJson(jsonObject))
            binding.edtSendMessage.setText("")
            binding.edtSendMessage.clearFocus()
            hideSoftKeyboard(binding.edtSendMessage)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun emitChooseAOption(helpOption: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", helpOption)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("chooseAOption", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun emitNextOption(option: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", option)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("chooseNextOption", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun emitDiseasePossibilities(helpOption: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", helpOption)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("chooseAPossibilities", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun emitPatientSideEffects(sideEffects: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", sideEffects)
            jsonObject.put("chatid", chatId)
            // jsonObject.put("isSent", false)
            webSocket!!.send(jsonObject.toString())
            Log.d("sideEffects", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun emitChooseSecondOption(helpOption: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", helpOption)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("chooseAOption", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    companion object {
        private var chatId = ""
        // private var isConnect = false
    }

    private fun isConnect(): Boolean {
        return isConnect
    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun disconnectSocket() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user", chatId)
            jsonObject.put("chatid", chatId)
            webSocket!!.send(jsonObject.toString())
            Log.d("disconnectSocket", Gson().toJson(jsonObject))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    /*override fun onBackPressed() {
        super.onBackPressed()
        // disconnectSocket()
        webSocket?.close(1,"CHAT_CLOSE")
        this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }*/
    fun disableEditText(editText: EditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)
    }

    fun enableEditText() {
        binding.edtSendMessage.requestFocus()
        binding.edtSendMessage.isFocusable = true
        binding.edtSendMessage.isEnabled = true
        binding.edtSendMessage.isCursorVisible = true
    }

    private infix fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        vg.isEnabled = enable // the point that I was missing
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            child.isClickable = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    /*override fun onUserInteraction() {
        super.onUserInteraction()
        stopHandler() // stop first and then start
        startHandler()
    }*/

    /*private fun stopHandler() {
        handler!!.removeCallbacks(r!!)
    }

    private fun startHandler() {
        handler!!.postDelayed(r!!, (1 * 60 * 1000).toLong())
    }*/

    private fun showErrorMsg(msg: String, view: View) {
        val snackbar = Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_LONG
        )

        val snack_root_view = snackbar.view
        snackbar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        val snack_text_view = snack_root_view
            .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        stopHandler()
        startHandler()

        return super.onTouchEvent(event)
    }

    private fun startHandler(){
        mHandler.postDelayed(mRunnable, mTime)
    }

    private fun stopHandler(){
        mHandler.removeCallbacks(mRunnable)
    }

    fun isOtherSelected(value: Boolean) {
        if (value) {
            Log.d("isOtherSelected", value.toString())
            isOthersSelected = true
        } else {
            Log.d("isOtherSelected", value.toString())
            isOthersSelected = false
        }
    }

}