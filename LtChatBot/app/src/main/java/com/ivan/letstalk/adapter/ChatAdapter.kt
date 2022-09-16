package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.model.chat.ChatPossibleProblems
import com.ivan.letstalk.ui.ALKChatActivity
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(applicationContext: Context, private val inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: MutableList<JSONObject> = ArrayList()
    private var sideEffectsChipItems = ArrayList<String>()
    private var mSideEffectsChipItems = mutableListOf<String>()
    private var possibleProblemsList = ArrayList<String>()
    private var possibleProblemsTitleList = ArrayList<String>()
    private var noSideEffectsList = ArrayList<String>()
    private var sideEffectsText = ArrayList<String>()
    private var sideEffectsTitle = ArrayList<String>()
    private val selectedChipList = mutableListOf<String>()
    var context: Context = applicationContext
    private val FADE_DURATION = 1500
    private lateinit var mHolder: ReceivedMessageHolder
    private var helpSelectedItem = ""
    private var sideEffectsSelectedItem = ""
    private var sideEffectsDurationSelectedItem = ""
    private var chatPossibleProblemsPosition = -1
    var processingClick = false
    var previousSideEffectClick = false
    var mClick = false

    private inner class SentMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTxt: TextView
        var tvTimeStamp: TextView

        init {
            messageTxt = itemView.findViewById(R.id.tv_sent)
            tvTimeStamp = itemView.findViewById(R.id.tv_time_stamp)
        }
    }

    private inner class ReceivedMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tvGreetings: TextView
        var tvReceiveTimeStamp: TextView
        var tvReceiveTwoTimeStamp: TextView
        var tvReceiveThreeTimeStamp: TextView
        var tvFifthTimeStamp: TextView
        var tvTkiTimeStamp: TextView
        // var tvSixthTimeStamp : TextView
        var tvHelp: TextView
        var cvHelp: MaterialCardView
        var cvGreetings: MaterialCardView
        var cvSideEffectsIssue: MaterialCardView
        var cvIssueDuration: MaterialCardView
        var cvPossibleProblems: MaterialCardView
        var cvPossibleProblemsFinal : MaterialCardView
        var cvStopTki: MaterialCardView
        var tvStopTki : TextView
        var tvSideEffects: TextView
        var tvHealthDetails: TextView
        var tvPossibleProblemText : TextView
        var tvPossibleProblemTextTwo : TextView
        var rvPossibleProblems: RecyclerView

        var tvPossibleProblemTextFinal: TextView
        var rvPossibleProblemsFinal : RecyclerView
        var tvPossibleProblemsFinalTimestamp: TextView
        var tvPossibleProblemsTimestamp: TextView
        var llBtn: LinearLayoutCompat

        var tvEmergencyCare: TextView
        var tvMedicineRoutine: TextView
        var tvUpdateVitals: TextView
        var rvMedicineRoutine: RelativeLayout
        var rvSideEffects: RelativeLayout
        var rvUpdateVitals: RelativeLayout
        var rvEmergencyCare: RelativeLayout
        var chipSideEffects: ChipGroup
        var rlChipGroup: RelativeLayout
        var cvSideEffects: MaterialCardView
        var tvSideEffectText: TextView
        var tvSideEffectSupportText: TextView

        var tvSideEffectsText: TextView
        var tvReceiveFourTimeStamp: TextView
        var tvExistingSideEffects: TextView
        var tvPreviousSideEffects: TextView
        var tvNewSideEffects: TextView
        var tvTapText: TextView
        var tvIssueText: TextView
        var tvLast48Hours: TextView
        var tvLastWeek: TextView
        var tvLastMonth: TextView
        var tvMoreThanAMonth: TextView

        var rvExistingSideEffects: RelativeLayout
        var rvPreviousSideEffects: RelativeLayout
        var rvNewSideEffects: RelativeLayout
        var rvLast48Hours: RelativeLayout
        var rvLastWeek: RelativeLayout
        var rvLastMonth: RelativeLayout
        var rvMoreThanAMonth: RelativeLayout

        var btnOk: Button
        var btnCancel: Button

        init {
            tvGreetings = itemView.findViewById(R.id.tv_greetings)
            tvReceiveTimeStamp = itemView.findViewById(R.id.tv_receive_time_stamp)
            tvReceiveTwoTimeStamp = itemView.findViewById(R.id.tv_receive_two_time_stamp)
            tvReceiveThreeTimeStamp = itemView.findViewById(R.id.tv_receive_three_time_stamp)
            tvFifthTimeStamp = itemView.findViewById(R.id.tv_fifth_time_stamp)
            tvTkiTimeStamp = itemView.findViewById(R.id.tv_tki_time_stamp)
            cvHelp = itemView.findViewById(R.id.cv_help)
            cvGreetings = itemView.findViewById(R.id.cv_greetings)
            cvSideEffectsIssue = itemView.findViewById(R.id.cv_side_effects_issue)
            cvIssueDuration = itemView.findViewById(R.id.cv_issue_duration)
            cvPossibleProblems = itemView.findViewById(R.id.cv_possible_problems)
            cvPossibleProblemsFinal = itemView.findViewById(R.id.cv_possible_problems_final)
            cvStopTki = itemView.findViewById(R.id.cv_stop_tki)
            tvStopTki = itemView.findViewById(R.id.tv_stop_tki)
            tvSideEffects = itemView.findViewById(R.id.tv_side_effects)
            tvHealthDetails = itemView.findViewById(R.id.tv_health_details)

            tvHelp = itemView.findViewById(R.id.tv_help)
            tvEmergencyCare = itemView.findViewById(R.id.tv_emergency_care)
            tvMedicineRoutine = itemView.findViewById(R.id.tv_medicine_routine)
            tvUpdateVitals = itemView.findViewById(R.id.tv_update_vitals)
            rvMedicineRoutine = itemView.findViewById(R.id.rv_medicine_routine)
            rvSideEffects = itemView.findViewById(R.id.rv_side_effects)
            rvUpdateVitals = itemView.findViewById(R.id.rv_update_vitals)
            rvEmergencyCare = itemView.findViewById(R.id.rv_emergency_care)
            chipSideEffects = itemView.findViewById(R.id.chip_side_effects)

            rlChipGroup = itemView.findViewById(R.id.rl_chip_group)
            cvSideEffects = itemView.findViewById(R.id.cv_side_effects)
            tvSideEffectText = itemView.findViewById(R.id.tv_side_effect_text)
            tvSideEffectSupportText = itemView.findViewById(R.id.tv_side_effect_support_text)
            btnOk = itemView.findViewById(R.id.btn_ok)
            btnCancel = itemView.findViewById(R.id.btn_cancel)

            tvSideEffectsText = itemView.findViewById(R.id.tv_side_effects_text)
            tvReceiveFourTimeStamp = itemView.findViewById(R.id.tv_receive_four_time_stamp)
            tvExistingSideEffects = itemView.findViewById(R.id.tv_existing_side_effects)
            tvPreviousSideEffects = itemView.findViewById(R.id.tv_previous_side_effects)
            tvNewSideEffects = itemView.findViewById(R.id.tv_new_side_effects)
            tvTapText = itemView.findViewById(R.id.tv_tap_text)
            tvIssueText = itemView.findViewById(R.id.tv_issue_text)
            tvLast48Hours = itemView.findViewById(R.id.tv_last_48_hours)
            tvLastWeek = itemView.findViewById(R.id.tv_last_week)
            tvLastMonth = itemView.findViewById(R.id.tv_last_month)
            tvMoreThanAMonth =itemView.findViewById(R.id.tv_more_than_a_month)

            rvExistingSideEffects = itemView.findViewById(R.id.rv_existing_side_effects)
            rvPreviousSideEffects = itemView.findViewById(R.id.rv_previous_side_effects)
            rvNewSideEffects = itemView.findViewById(R.id.rv_new_side_effects)

            rvLast48Hours = itemView.findViewById(R.id.rv_last_48_hours)
            rvLastWeek = itemView.findViewById(R.id.rv_last_week)
            rvLastMonth = itemView.findViewById(R.id.rv_last_month)
            rvMoreThanAMonth = itemView.findViewById(R.id.rv_more_than_a_month)
            rvPossibleProblems = itemView.findViewById(R.id.rv_possible_problems)

            tvPossibleProblemText = itemView.findViewById(R.id.tv_possible_problem_text)
            tvPossibleProblemTextTwo = itemView.findViewById(R.id.tv_possible_problem_text_two)

            tvPossibleProblemTextFinal = itemView.findViewById(R.id.tv_possible_problem_text_final)
            rvPossibleProblemsFinal = itemView.findViewById(R.id.rv_possible_problems_final)
            tvPossibleProblemsFinalTimestamp = itemView.findViewById(R.id.tv_possible_problems_final_timestamp)
            tvPossibleProblemsTimestamp = itemView.findViewById(R.id.tv_possible_problems_timestamp)

            llBtn = itemView.findViewById(R.id.ll_btn)
            // tvSixthTimeStamp = itemView.findViewById(R.id.tv_sixth_time_stamp)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        Log.d("check_message", "message_list: ${Gson().toJson(message)}")
        try {
            return if (message.getBoolean("isSent")) {
                TYPE_MESSAGE_SENT
            } else {
                TYPE_MESSAGE_RECEIVED
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    /*override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        try {
            if (message.has("greetings")) {
                TYPE_MESSAGE_SENT
            } else if (message.has("patient_issue")){
                PATIENT_ISSUE
            } else if (message.has("side_effects_list")){
                SIDE_EFFECT_LIST
            } else {
                PATIENT_REQUEST
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            TYPE_MESSAGE_SENT -> {
                view = inflater.inflate(R.layout.chat_send_message_item, parent, false)
                return SentMessageHolder(view)
            }
            TYPE_MESSAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.chat_message_received_item, parent, false)
                return ReceivedMessageHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
       /* view = inflater.inflate(R.layout.chat_send_message_item, parent, false)
        return SentMessageHolder(view)*/
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addItem(jsonObject: JSONObject) {
        messages.add(jsonObject)
        notifyDataSetChanged()
    }
    private fun getChip(text: String): Chip {
        val chip = Chip(context)
        chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 50f,
            context.resources.displayMetrics
        ).toInt()
        chip.setChipBackgroundColorResource(R.color.divider_color)
        chip.setTextColor(ContextCompat.getColor(context, R.color.black))
        chip.isCloseIconVisible = false
        chip.isCheckedIconVisible = false
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = context.getColorStateList(R.color.chat_answer_select)
                }
                chip.setTextColor(ContextCompat.getColor(context, R.color.white))
                chip.isChecked = true
                sideEffectsChipItems.add(chip.text.toString())
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = context.getColorStateList(R.color.divider_color)
                }
                chip.setTextColor(ContextCompat.getColor(context, R.color.black))
                chip.isChecked = false
                sideEffectsChipItems.remove(chip.text.toString())
            }
        }
        return chip
    }

    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    companion object {
        private const val TYPE_MESSAGE_SENT = 0
        private const val TYPE_MESSAGE_RECEIVED = 1
        /*private const val MESSAGE_GREETINGS = 2
        private const val PATIENT_ISSUE = 3
        private const val SIDE_EFFECT_LIST = 4
        private const val PATIENT_REQUEST = 5*/
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // holder.setIsRecyclable(false)
        // mHolder = holder as ReceivedMessageHolder
        val message = messages[position]
        try {
            if (message.getBoolean("isSent")) {
                val messageHolder = holder as SentMessageHolder
                Log.d("adapter_message",message.toString())
                if (message.getString("message_type").equals("patient_request")) {
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    if (responseText.getJSONObject(0).has("name")) {
                        val sideEffects = responseText.getJSONObject(0).getString("name")
                        setFadeAnimation(messageHolder.itemView)
                        messageHolder.messageTxt.text = sideEffects
                    }
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvTimeStamp.visibility = View.VISIBLE
                    messageHolder.tvTimeStamp.text = splitTime
                    /*val option = dataJsonObject.getString("option")
                    Log.d("n_option",option)
                    if (!mClick) {
                        if (option.isNotEmpty()) {
                            (context as ALKChatActivity).emitNextOption(
                                option
                            )
                            mClick = true
                        }
                    }*/
                } else if (message.getString("message_type").equals("first_message")) {
                    val simpleDateFormat = SimpleDateFormat("hh:mm a")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.tvTimeStamp.text = currentDateAndTime
                    if (message.getString("user").isNotEmpty()){
                        messageHolder.messageTxt.text = message.getString("user")
                    }
                } else if (message.getString("message_type").equals("do_not_know")){
                    val mMessage = message.getString("message")
                    // showErrorMsg(mMessage, messageHolder.itemView)
                    // messageHolder.messageTxt.text = mMessage
                    val simpleDateFormat = SimpleDateFormat("hh:mm a")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())
                    // messageHolder.tvTimeStamp.text = currentDateAndTime
                }
            } else {
                if (message.getString("message_type").equals("greetings")) {
                    val messageHolder = holder as ReceivedMessageHolder
                    messageHolder.cvGreetings.visibility = View.VISIBLE
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.rlChipGroup.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    // messageHolder.cvStopTki.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE

                    setFadeAnimation(messageHolder.itemView)
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val greetingsMsg = responseText.getJSONObject(0).getString("name")
                    val patientHealthDetails = responseText.getJSONObject(1).getString("name")
                    Log.d("patientHealthDetails",patientHealthDetails)
                    /*val simpleDateFormat = SimpleDateFormat("hh:mm a")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())*/
                    messageHolder.tvGreetings.text = Html.fromHtml(greetingsMsg)
                    if (patientHealthDetails == null) {
                        messageHolder.tvHealthDetails.text = Html.fromHtml("No Data")
                    } else {
                        messageHolder.tvHealthDetails.text = Html.fromHtml(patientHealthDetails)
                    }
                    messageHolder.tvReceiveTimeStamp.visibility = View.VISIBLE
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvReceiveTimeStamp.text = splitTime
                } else if (message.getString("message_type").equals("patient_issue")) {
                    Log.d("patient_issue", "This is Patient Issue")
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    /*messageHolder.cvHelp.visibility = View.VISIBLE
                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvSideEffects.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    messageHolder.tvReceiveTimeStamp.visibility = View.GONE*/
                    val dataJsonObject = message.getJSONObject("data")
                    val responseId = dataJsonObject.getString("response_id")
                    if (responseId.equals("2")) {
                        Log.d("Tap an option","Tap an option")
                        messageHolder.cvHelp.visibility = View.VISIBLE
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvSideEffects.visibility = View.GONE
                        // messageHolder.rlChipGroup.visibility = View.GONE
                        messageHolder.cvSideEffectsIssue.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        // Timestamp
                        messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                        messageHolder.tvTkiTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveThreeTimeStamp.visibility = View.GONE
                        messageHolder.tvFifthTimeStamp.visibility = View.GONE
                        messageHolder.tvTkiTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveFourTimeStamp.visibility = View.GONE
                        messageHolder.tvPossibleProblemsTimestamp.visibility = View.GONE
                        Log.d("responseId",responseId)
                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val tapOption = responseText.getJSONObject(0).getString("name")
                        val sideEffects = responseText.getJSONObject(1).getString("name")
                        val emergencyCare = responseText.getJSONObject(2).getString("name")
                        val medicineRoutine = responseText.getJSONObject(3).getString("name")
                        val updateVitals = responseText.getJSONObject(4).getString("name")
                        Log.d("sideEffects", sideEffects)
                        messageHolder.tvHelp.text = tapOption
                        messageHolder.tvSideEffects.text = sideEffects
                        messageHolder.tvEmergencyCare.text = emergencyCare
                        /*messageHolder.tvUpdateVitals.visibility = View.GONE
                        messageHolder.tvMedicineRoutine.visibility = View.GONE
                        messageHolder.rvMedicineRoutine.visibility = View.GONE
                        messageHolder.rvUpdateVitals.visibility = View.GONE*/
                        messageHolder.tvMedicineRoutine.text = medicineRoutine
                        messageHolder.tvUpdateVitals.text = updateVitals
                        /*val simpleDateFormat = SimpleDateFormat("hh:mm a")
                        val currentDateAndTime: String = simpleDateFormat.format(Date())
                        messageHolder.tvReceiveTwoTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvReceiveTwoTimeStamp.text = currentDateAndTime*/
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("date", splitTime)
                        }
                        messageHolder.tvReceiveTwoTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvReceiveTwoTimeStamp.text = splitTime

                        Log.d("helpSelectedItem",helpSelectedItem)
                        if (helpSelectedItem == "I am suffering from SIDE EFFECTS") {
                            messageHolder.tvSideEffects.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                            messageHolder.rvSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvSideEffects.setPadding(20, 20, 20, 20)
                            disableEnableControls(false, messageHolder.cvHelp)
                        } /*else if (helpSelectedItem == "I need EMERGENCY CARE") {
                            messageHolder.tvEmergencyCare.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                            messageHolder.rvEmergencyCare.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvEmergencyCare.setPadding(20, 20, 20, 20)
                        }*/

                        messageHolder.rvSideEffects.setOnClickListener {
                            (context as ALKChatActivity).emitChooseAOption(sideEffects)
                            messageHolder.rvSideEffects.isEnabled = false
                            messageHolder.rvSideEffects.isClickable = false
                            messageHolder.tvSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvHelp)
                            Log.d("holderPosition",holder.absoluteAdapterPosition.toString())
                            helpSelectedItem = sideEffects
                            /*if (holder.absoluteAdapterPosition.toString() == "2") {
                                disableEnableControls(false, messageHolder.cvHelp)
                            }
                            disableEnableControls(false, messageHolder.cvHelp)*/
                        }
                        messageHolder.rvEmergencyCare.setOnClickListener {
                            helpSelectedItem = emergencyCare
                            (context as ALKChatActivity).emitChooseAOption(emergencyCare)
                            messageHolder.rvEmergencyCare.isEnabled = false
                            messageHolder.rvEmergencyCare.isClickable = false
                            messageHolder.tvEmergencyCare.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvEmergencyCare.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvEmergencyCare.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvHelp)
                        }

                        messageHolder.rvMedicineRoutine.setOnClickListener {
                            helpSelectedItem = medicineRoutine
                            (context as ALKChatActivity).emitChooseAOption(medicineRoutine)
                            messageHolder.rvMedicineRoutine.isEnabled = false
                            messageHolder.rvMedicineRoutine.isClickable = false
                            messageHolder.tvMedicineRoutine.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvMedicineRoutine.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvMedicineRoutine.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvHelp)
                        }

                        messageHolder.rvUpdateVitals.setOnClickListener {
                            helpSelectedItem = updateVitals
                            (context as ALKChatActivity).emitChooseAOption(updateVitals)
                            messageHolder.rvUpdateVitals.isEnabled = false
                            messageHolder.rvUpdateVitals.isClickable = false
                            messageHolder.tvUpdateVitals.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvUpdateVitals.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvUpdateVitals.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvHelp)
                        }
                    }
                    if (responseId == "29") {
                        Log.d("responseId", responseId + "29")
                        Log.d("m_message", message.toString())
                        messageHolder.cvSideEffectsIssue.visibility = View.VISIBLE
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvSideEffects.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        // Timestamp
                        messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveTwoTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveThreeTimeStamp.visibility = View.GONE
                        messageHolder.tvFifthTimeStamp.visibility = View.GONE
                        messageHolder.tvTkiTimeStamp.visibility = View.GONE
                        messageHolder.tvPossibleProblemsTimestamp.visibility = View.GONE
                        /*var tvReceiveThreeTimeStamp: TextView
                        var tvFifthTimeStamp: TextView
                        var tvTkiTimeStamp: TextView*/
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.GONE

                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val hereAreYour = responseText.getJSONObject(0).getString("name")
                        val existingSideEffects = responseText.getJSONObject(1).getString("name")
                        // val previousSideEffects = responseText.getJSONObject(2).getString("name")
                        val tapText = responseText.getJSONObject(2).getString("name")
                        val newSideEffects = responseText.getJSONObject(3).getString("name")
                        messageHolder.tvNewSideEffects.text = newSideEffects
                        messageHolder.tvTapText.text = tapText
                        messageHolder.tvPreviousSideEffects
                        // messageHolder.tvPreviousSideEffects.text = previousSideEffects
                        messageHolder.tvExistingSideEffects.text = existingSideEffects
                        messageHolder.tvSideEffectsText.text = hereAreYour
                        messageHolder.tvReceiveFourTimeStamp.visibility = View.VISIBLE

                        messageHolder.rvPreviousSideEffects.visibility = View.GONE
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("date", splitTime)
                        }
                        messageHolder.tvReceiveFourTimeStamp.text = splitTime
                        when (sideEffectsSelectedItem) {
                            "Existing SIDE EFFECTS" -> {
                                messageHolder.tvExistingSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvExistingSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvExistingSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }
                            /*"Previous SIDE EFFECTS" -> {
                                messageHolder.tvPreviousSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvPreviousSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvPreviousSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }*/
                            "NEW SIDE EFFECT" -> {
                                messageHolder.tvNewSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvNewSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvNewSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }
                        }

                        messageHolder.rvExistingSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = existingSideEffects
                            (context as ALKChatActivity).emitChooseAOption(existingSideEffects)
                            messageHolder.tvExistingSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvExistingSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvExistingSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }
                        /*messageHolder.rvPreviousSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = previousSideEffects
                            (context as ALKChatActivity).emitChooseAOption(previousSideEffects)
                            messageHolder.tvPreviousSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvPreviousSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvPreviousSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }*/
                        messageHolder.rvNewSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = newSideEffects
                            (context as ALKChatActivity).emitChooseAOption(newSideEffects)
                            messageHolder.tvNewSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvNewSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvNewSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }
                    }
                    // Emergency Care is selected
                    if (responseId == "24") {
                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val stopTki = responseText.getJSONObject(0).getString("name")
                        Log.d("stopTki", stopTki.toString())
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvSideEffectsIssue.visibility = View.GONE
                        messageHolder.chipSideEffects.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.VISIBLE
                        messageHolder.tvStopTki.text = stopTki.toString()
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("date", splitTime)
                        }
                        messageHolder.tvTkiTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvTkiTimeStamp.text = splitTime
                    }
                    // Medicine Routine is Selected
                    if (responseId == "25") {
                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val stopTki = responseText.getJSONObject(0).getString("name")
                        Log.d("stopTki", stopTki.toString())
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvSideEffectsIssue.visibility = View.GONE
                        messageHolder.chipSideEffects.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.VISIBLE
                        messageHolder.tvStopTki.text = stopTki.toString()
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("splitTime", splitTime)
                        }
                        messageHolder.tvTkiTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvTkiTimeStamp.text = splitTime
                    }

                    // Update Vitals is Selected
                    if (responseId == "26") {
                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val stopTki = responseText.getJSONObject(0).getString("name")
                        Log.d("stopTki", stopTki.toString())
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvSideEffectsIssue.visibility = View.GONE
                        messageHolder.chipSideEffects.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.VISIBLE
                        messageHolder.tvStopTki.text = stopTki.toString()
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("splitTime", splitTime)
                        }
                        messageHolder.tvTkiTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvTkiTimeStamp.text = splitTime
                    }

                    if (responseId == "5") {
                        Log.d("responseId", responseId + "5")
                        messageHolder.cvSideEffectsIssue.visibility = View.VISIBLE
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.GONE
                        messageHolder.cvSideEffects.visibility = View.GONE
                        messageHolder.cvPossibleProblems.visibility = View.GONE
                        // Timestamp
                        messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveTwoTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveThreeTimeStamp.visibility = View.GONE
                        messageHolder.tvFifthTimeStamp.visibility = View.GONE
                        messageHolder.tvTkiTimeStamp.visibility = View.GONE
                        messageHolder.tvPossibleProblemsTimestamp.visibility = View.GONE
                        /*var tvReceiveThreeTimeStamp: TextView
                        var tvFifthTimeStamp: TextView
                        var tvTkiTimeStamp: TextView*/
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.GONE

                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val hereAreYour = responseText.getJSONObject(0).getString("name")
                        val existingSideEffects = responseText.getJSONObject(1).getString("name")
                        val previousSideEffects = responseText.getJSONObject(2).getString("name")
                        val tapText = responseText.getJSONObject(3).getString("name")
                        val newSideEffects = responseText.getJSONObject(4).getString("name")
                        messageHolder.tvNewSideEffects.text = newSideEffects
                        messageHolder.tvTapText.text = tapText
                        messageHolder.tvPreviousSideEffects.text = previousSideEffects
                        messageHolder.tvExistingSideEffects.text = existingSideEffects
                        messageHolder.tvSideEffectsText.text = hereAreYour
                        messageHolder.tvReceiveFourTimeStamp.visibility = View.VISIBLE
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("date", splitTime)
                        }
                        messageHolder.tvReceiveFourTimeStamp.text = splitTime
                        when (sideEffectsSelectedItem) {
                            "Existing SIDE EFFECTS" -> {
                                messageHolder.tvExistingSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvExistingSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvExistingSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }
                            "Previous SIDE EFFECTS" -> {
                                messageHolder.tvPreviousSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvPreviousSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvPreviousSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }
                            "NEW SIDE EFFECT" -> {
                                messageHolder.tvNewSideEffects.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvNewSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvNewSideEffects.setPadding(20, 20, 20, 20)
                                disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                            }
                        }

                        messageHolder.rvExistingSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = existingSideEffects
                            (context as ALKChatActivity).emitChooseAOption(existingSideEffects)
                            messageHolder.tvExistingSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvExistingSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvExistingSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }
                        messageHolder.rvPreviousSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = previousSideEffects
                            (context as ALKChatActivity).emitChooseAOption(previousSideEffects)
                            messageHolder.tvPreviousSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvPreviousSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvPreviousSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }
                        messageHolder.rvNewSideEffects.setOnClickListener {
                            sideEffectsSelectedItem = newSideEffects
                            (context as ALKChatActivity).emitChooseAOption(newSideEffects)
                            messageHolder.tvNewSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvNewSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvNewSideEffects.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvSideEffectsIssue)
                        }
                    }

                    if (responseId == "12") {
                        Log.d("responseId",responseId+"12")
                        messageHolder.cvGreetings.visibility = View.GONE
                        messageHolder.cvSideEffectsIssue.visibility = View.GONE
                        messageHolder.chipSideEffects.visibility = View.GONE
                        messageHolder.cvHelp.visibility = View.GONE
                        messageHolder.cvStopTki.visibility = View.GONE
                        messageHolder.cvSideEffects.visibility = View.GONE

                        messageHolder.tvReceiveTwoTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveThreeTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                        messageHolder.tvReceiveFourTimeStamp.visibility = View.GONE
                        messageHolder.tvTkiTimeStamp.visibility = View.GONE
                        messageHolder.tvPossibleProblemsTimestamp.visibility = View.GONE
                        // messageHolder.tvFifthTimeStamp.visibility = View.GONE
                        messageHolder.cvIssueDuration.visibility = View.VISIBLE
                        val responseText = dataJsonObject.getJSONArray("response_text")
                        val headerText = responseText.getJSONObject(0).getString("name")
                        val within48HoursText = responseText.getJSONObject(1).getString("name")
                        val lastWeekText = responseText.getJSONObject(2).getString("name")
                        val lastMonthText = responseText.getJSONObject(3).getString("name")
                        val moreThanAMonth = responseText.getJSONObject(4).getString("name")
                        messageHolder.tvMoreThanAMonth.text = moreThanAMonth
                        messageHolder.tvLastMonth.text = lastMonthText
                        messageHolder.tvLastWeek.text = lastWeekText
                        messageHolder.tvLast48Hours.text = within48HoursText
                        messageHolder.tvIssueText.text = headerText
                        val time = message.getString("datetime")
                        var str1 = ""
                        if (time.isNotEmpty()){
                            str1 = time
                        }
                        var splitTime = ""
                        if (str1.isNotEmpty()) {
                            splitTime = str1.split("\\s".toRegex())[1]
                            Log.d("date", splitTime)
                        }
                        messageHolder.tvFifthTimeStamp.visibility = View.VISIBLE
                        messageHolder.tvFifthTimeStamp.text = splitTime
                        when (sideEffectsDurationSelectedItem) {
                            "Within last 48 hrs" -> {
                                messageHolder.tvLast48Hours.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvLast48Hours.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvLast48Hours.setPadding(20, 20, 20, 20)
                            }
                            "Within last week" -> {
                                messageHolder.tvLastWeek.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvLastWeek.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvLastWeek.setPadding(20, 20, 20, 20)
                            }
                            "Within last month" -> {
                                messageHolder.tvLastMonth.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvLastMonth.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvLastMonth.setPadding(20, 20, 20, 20)
                            }
                            "More than a month" -> {
                                messageHolder.tvMoreThanAMonth.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.white
                                    )
                                )
                                messageHolder.rvMoreThanAMonth.setBackgroundResource(R.drawable.layout_red_back)
                                messageHolder.rvMoreThanAMonth.setPadding(20, 20, 20, 20)
                            }
                        }

                        messageHolder.rvLast48Hours.setOnClickListener {
                            sideEffectsDurationSelectedItem = within48HoursText
                            (context as ALKChatActivity).emitChooseAOption(within48HoursText)
                            messageHolder.tvLast48Hours.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvLast48Hours.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvLast48Hours.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvIssueDuration)
                        }

                        messageHolder.rvLastWeek.setOnClickListener {
                            sideEffectsDurationSelectedItem = lastWeekText
                            (context as ALKChatActivity).emitChooseAOption(lastWeekText)
                            messageHolder.tvLastWeek.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvLastWeek.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvLastWeek.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvIssueDuration)
                        }

                        messageHolder.rvLastMonth.setOnClickListener {
                            sideEffectsDurationSelectedItem = lastMonthText
                            (context as ALKChatActivity).emitChooseAOption(lastMonthText)
                            messageHolder.tvLastMonth.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvLastMonth.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvLastMonth.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvIssueDuration)
                        }

                        messageHolder.rvMoreThanAMonth.setOnClickListener {
                            sideEffectsDurationSelectedItem = lastMonthText
                            (context as ALKChatActivity).emitChooseAOption(lastMonthText)
                            messageHolder.tvMoreThanAMonth.setTextColor(ContextCompat.getColor(context,R.color.white))
                            messageHolder.rvMoreThanAMonth.setBackgroundResource(R.drawable.layout_red_back)
                            messageHolder.rvMoreThanAMonth.setPadding(20,20,20,20)
                            disableEnableControls(false, messageHolder.cvIssueDuration)
                        }
                    }
                    /*val responseText = dataJsonObject.getJSONArray("response_text")
                    val tapOption = responseText.getJSONObject(0).getString("0")
                    val sideEffects = responseText.getJSONObject(1).getString("1")
                    val emergencyCare = responseText.getJSONObject(2).getString("2")
                    val medicineRoutine = responseText.getJSONObject(3).getString("3")
                    if (responseText.getJSONObject(3).getString("action_type").equals("text")) {
                        messageHolder.rvMedicineRoutine.setBackgroundColor(Color.parseColor("#ffffff"))
                    } else {
                        messageHolder.rvMedicineRoutine.setBackgroundResource(R.drawable.chat_child_blue_back)
                    }
                    val updateVitals = responseText.getJSONObject(4).getString("4")
                    Log.d("sideEffects", sideEffects)
                    messageHolder.tvHelp.text = tapOption
                    messageHolder.tvSideEffects.text = sideEffects
                    messageHolder.tvEmergencyCare.text = emergencyCare
                    messageHolder.tvMedicineRoutine.text = medicineRoutine
                    messageHolder.tvUpdateVitals.text = updateVitals
                    val simpleDateFormat = SimpleDateFormat("hh:mm a")
                    val currentDateAndTime: String = simpleDateFormat.format(Date())
                    messageHolder.tvReceiveTwoTimeStamp.text = currentDateAndTime

                    messageHolder.rvSideEffects.setOnClickListener {
                        (context as ALKChatActivity).emitChooseAOption(sideEffects)
                        messageHolder.rvSideEffects.isEnabled = false
                        messageHolder.rvSideEffects.isClickable = false
                        messageHolder.tvSideEffects.setTextColor(ContextCompat.getColor(context,R.color.white))
                        messageHolder.rvSideEffects.setBackgroundResource(R.drawable.layout_red_back)
                        messageHolder.rvSideEffects.setPadding(20,20,20,20)
                    }
                    messageHolder.rvEmergencyCare.setOnClickListener {
                        (context as ALKChatActivity).emitChooseAOption(emergencyCare)
                        messageHolder.rvEmergencyCare.isEnabled = false
                        messageHolder.rvEmergencyCare.isClickable = false
                        messageHolder.tvEmergencyCare.setTextColor(ContextCompat.getColor(context,R.color.white))
                        messageHolder.rvEmergencyCare.setBackgroundResource(R.drawable.layout_red_back)
                        messageHolder.rvEmergencyCare.setPadding(20,20,20,20)
                    }
                    messageHolder.rvUpdateVitals.setOnClickListener {
                        (context as ALKChatActivity).emitChooseSecondOption(updateVitals)
                        messageHolder.rvUpdateVitals.isEnabled = false
                        messageHolder.rvUpdateVitals.isClickable = false
                        messageHolder.tvUpdateVitals.setTextColor(ContextCompat.getColor(context,R.color.white))
                        messageHolder.rvUpdateVitals.setBackgroundResource(R.drawable.layout_red_back)
                        messageHolder.rvUpdateVitals.setPadding(10,10,10,10)
                    }*/
                } else if (message.getString("message_type").equals("side_effects_list")) {
                    // Refresh the list
                    mSideEffectsChipItems.clear()
                    Log.d("side_effect", message.toString())
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val messageHolder = holder as ReceivedMessageHolder

                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.cvGreetings.visibility = View.GONE
                    // messageHolder.cvSideEffects.visibility = View.VISIBLE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvStopTki.visibility = View.GONE
                    // messageHolder.cvGreetings.visibility = View.INVISIBLE
                    messageHolder.tvHelp.text = message.toString()
                    /*messageHolder.tvSideEffects.visibility = View.GONE
                    messageHolder.tvEmergencyCare.visibility = View.GONE
                    messageHolder.tvMedicineRoutine.visibility = View.GONE
                    messageHolder.tvUpdateVitals.visibility = View.GONE*/

                    messageHolder.chipSideEffects.visibility = View.VISIBLE
                    messageHolder.rlChipGroup.visibility = View.VISIBLE
                    messageHolder.cvSideEffects.visibility = View.VISIBLE
                    messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvReceiveThreeTimeStamp.text = splitTime
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("checkbox")) {
                            // sideEffectsChipItems.add(responseText.getJSONObject(i).getString("name"))
                            addToList(responseText.getJSONObject(i).getString("name"))
                            // mSideEffectsChipItems.add(responseText.getJSONObject(i).getString("name"))
                            if (responseText.getJSONObject(i).getString("name").equals("Other")) {

                            }
                        }
                    }
                    Log.d("side_effect_list", Gson().toJson(mSideEffectsChipItems))
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("text")) {
                            sideEffectsText.add(responseText.getJSONObject(i).getString("name"))
                        }
                    }
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("title")) {
                            sideEffectsTitle.add(responseText.getJSONObject(i).getString("name"))
                        }
                    }
                    Log.d("side_effect_text", Gson().toJson(sideEffectsText))
                    messageHolder.tvSideEffectText.text = sideEffectsTitle[0]
                    // messageHolder.tvSideEffectSupportText.text = sideEffectsText[1]
                    if (1 in sideEffectsText.indices) {
                        messageHolder.tvSideEffectSupportText.text = sideEffectsText[1]
                    }
                    /*if (sideEffectsText[1].isNotEmpty()) {
                        messageHolder.tvSideEffectSupportText.text = sideEffectsText[1]
                    }*/
                    /*for (i in sideEffectsChipItems.indices) {
                        val entryChip2: Chip = getChip(sideEffectsChipItems[i])
                        entryChip2.id = i
                        //set default selected language
                        //entryChip2.setChecked(true);
                        messageHolder.chipSideEffects.addView(entryChip2)
                    }*/
                    messageHolder.chipSideEffects.removeAllViews()
                    for (i in mSideEffectsChipItems.indices) {
                        // val entryChip2: Chip = getChip(sideEffectsChipItems[i])
                        val entryChip2: Chip = getChip(mSideEffectsChipItems[i])
                        entryChip2.id = i
                        //set default selected language
                        //entryChip2.setChecked(true);
                        messageHolder.chipSideEffects.addView(entryChip2)
                    }
                    for (index in 0 until messageHolder.chipSideEffects.childCount) {
                        val chip: Chip = messageHolder.chipSideEffects.getChildAt(index) as Chip
                        // Set the chip checked change listener
                        if (selectedChipList != null && selectedChipList.size != 0)
                            for (i in selectedChipList.indices)
                                chip.isChecked = selectedChipList[i].equals(chip.text.toString())
                        chip.setOnCheckedChangeListener { view, isChecked ->
                            if (isChecked) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    chip.chipBackgroundColor =
                                        context.getColorStateList(R.color.chat_answer_select)
                                }
                                chip.setTextColor(ContextCompat.getColor(context, R.color.white))
                                chip.isChecked = true
                                selectedChipList.add(view.text.toString())
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    chip.chipBackgroundColor =
                                        context.getColorStateList(R.color.divider_color)
                                }
                                chip.setTextColor(ContextCompat.getColor(context, R.color.black))
                                chip.isChecked = false
                                selectedChipList.remove(view.text.toString())
                            }
                            messageHolder.btnOk.setOnClickListener {
                                if (selectedChipList.isNotEmpty() && !selectedChipList.contains("Other")) {
                                    val selectedIssues: String =
                                        java.lang.String.join(",", selectedChipList)
                                    /*var test = selectedChipList.toString()
                                    test = test.replace("\\[", "").replace("\\]","")
                                    Log.d("selectedChipList", selectedChipList.toString())*/
                                    Log.d("selectedChipList", selectedIssues)
                                    (context as ALKChatActivity).emitPatientSideEffects(
                                        selectedIssues
                                    )
                                }
                                if (selectedChipList.contains("Other")) {
                                    Log.d("other","other")
                                    (context as ALKChatActivity).enableEditText()
                                    (context as ALKChatActivity).isOtherSelected(true)
                                }
                            }
                            messageHolder.btnCancel.setOnClickListener {
                                // Cancel the selected chips
                                for (i in 0 until messageHolder.chipSideEffects.childCount) {
                                    (messageHolder.chipSideEffects.getChildAt(i) as Chip).isChecked =
                                        false
                                }
                            }
                        }
                    }
                } else if (message.getString("message_type").equals("disease_possibilties")) {
                    Log.d("disease_possibilties", message.toString())
                    val dataJsonObject = message.getJSONObject("data")
                    val responseId = dataJsonObject.getString("response_id")
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.chipSideEffects.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvSideEffects.visibility = View.GONE
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.VISIBLE
                    messageHolder.tvPossibleProblemTextTwo.text = ""

                    messageHolder.tvPossibleProblemsTimestamp.visibility = View.VISIBLE
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvPossibleProblemsTimestamp.text = splitTime

                    Log.d("responseId", responseId + "17")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val newList = ArrayList<ChatPossibleProblems>()
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("button")) {
                            possibleProblemsList.add(responseText.getJSONObject(i).getString("name"))
                            newList.add(ChatPossibleProblems(responseText.getJSONObject(i).getString("name")))
                        }
                    }
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("title")) {
                            possibleProblemsTitleList.add(responseText.getJSONObject(i).getString("name"))
                        }
                    }
                    Log.d("possible_problems", Gson().toJson(newList))
                    Log.d("possible_problems_title", Gson().toJson(possibleProblemsTitleList))
                    if (possibleProblemsTitleList.isNotEmpty()) {
                        if (0 in possibleProblemsTitleList.indices) {
                            messageHolder.tvPossibleProblemText.text =
                                possibleProblemsTitleList[0]
                        }
                    }
                    val chatPossibleProblemsAdapter = ChatPossibleProblemsAdapter(newList,object :ChatPossibleProblemsAdapter.onClickListener{
                        override fun onClick(pos: Int) {
                            chatPossibleProblemsPosition = pos
                            Log.d("chatPossibleProblemsPosition",pos.toString())
                            newList[pos].possibleProblems?.let {
                                (context as ALKChatActivity).emitDiseasePossibilities(
                                    it
                                )
                                Log.d("possible", it)
                            }
                        }

                        override fun onTextClick(data: ChatPossibleProblems?) {
                            data?.possibleProblems
                            data?.possibleProblems?.let { Log.d("pp", it) }
                        }

                    })
                    holder.rvPossibleProblems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    holder.rvPossibleProblems.adapter = chatPossibleProblemsAdapter
                } else if (message.getString("message_type").equals("stop_tki")) {
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val stopTki = responseText.getJSONObject(0).getString("name")
                    Log.d("stopTki", stopTki.toString())
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.chipSideEffects.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.rlChipGroup.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    messageHolder.cvStopTki.visibility = View.VISIBLE
                    messageHolder.tvStopTki.text = stopTki.toString()
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()) {
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("splitTime", splitTime)
                    }
                    messageHolder.tvTkiTimeStamp.visibility = View.VISIBLE
                    messageHolder.tvTkiTimeStamp.text = splitTime
                    Log.d("stop_option","Not Stop")
                    val option = dataJsonObject.getString("option")
                    if (option.isNotEmpty()) {
                        if (!option.equals("stop")) {
                            (context as ALKChatActivity).emitChooseAOption(
                                option
                            )
                        }
                    }
                } else if (message.getString("message_type").equals("disease_possibilties_final")) {
                    Log.d("disease_possibilties", message.toString())
                    val dataJsonObject = message.getJSONObject("data")
                    val responseId = dataJsonObject.getString("response_id")
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.chipSideEffects.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvSideEffects.visibility = View.GONE
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.VISIBLE
                    messageHolder.tvPossibleProblemTextTwo.text = ""
                    messageHolder.tvPossibleProblemsTimestamp.visibility = View.VISIBLE
                    messageHolder.tvTkiTimeStamp.visibility = View.GONE
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvPossibleProblemsTimestamp.text = splitTime
                    Log.d("responseId", responseId + "17")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val newList = ArrayList<ChatPossibleProblems>()
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("button")) {
                            possibleProblemsList.add(responseText.getJSONObject(i).getString("name"))
                            newList.add(ChatPossibleProblems(responseText.getJSONObject(i).getString("name")))
                        }
                    }
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("title")) {
                            possibleProblemsTitleList.add(responseText.getJSONObject(i).getString("name"))
                        }
                    }
                    Log.d("possible_problems", Gson().toJson(newList))
                    Log.d("possible_problems_title", Gson().toJson(possibleProblemsTitleList))
                    if (possibleProblemsTitleList.isNotEmpty()) {
                        if (0 in possibleProblemsTitleList.indices) {
                            messageHolder.tvPossibleProblemText.text =
                                possibleProblemsTitleList[0]
                        }
                    }
                    val chatPossibleProblemsAdapter = ChatPossibleProblemsAdapter(newList,object :ChatPossibleProblemsAdapter.onClickListener{
                        override fun onClick(pos: Int) {
                            newList[pos].possibleProblems?.let {
                                (context as ALKChatActivity).emitDiseasePossibilities(
                                    it
                                )
                                Log.d("possible", it)
                            }
                        }

                        override fun onTextClick(data: ChatPossibleProblems?) {
                            data?.possibleProblems
                            data?.possibleProblems?.let { Log.d("pp", it) }
                        }

                    })
                    holder.rvPossibleProblems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    holder.rvPossibleProblems.adapter = chatPossibleProblemsAdapter
                } else if (message.getString("message_type").equals("no_side_effects")) {
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.tvHelp.text = message.toString()
                    messageHolder.chipSideEffects.visibility = View.GONE
                    messageHolder.rlChipGroup.visibility = View.VISIBLE
                    messageHolder.cvSideEffects.visibility = View.VISIBLE
                    messageHolder.tvReceiveTimeStamp.visibility = View.GONE
                    messageHolder.llBtn.visibility = View.GONE
                    for (i in 0 until responseText.length()) {
                        if (responseText.getJSONObject(i).getString("action_type").equals("no_side_effect_text")) {
                            noSideEffectsList.add(responseText.getJSONObject(i).getString("name"))
                            messageHolder.tvSideEffectText.text = noSideEffectsList[0]
                        }
                    }
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()){
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("date", splitTime)
                    }
                    messageHolder.tvReceiveThreeTimeStamp.text = splitTime
                    val option = dataJsonObject.getString("option")
                    Log.d("n_option",option)
                    /*if (option.isNotEmpty()) {
                        (context as ALKChatActivity).emitChooseAOption(
                            option
                        )
                    }*/
                    if (!previousSideEffectClick) {
                        if (option.isNotEmpty()) {
                            (context as ALKChatActivity).emitNextOption(
                                option
                            )
                            previousSideEffectClick = true
                        }
                    }
                } else if (message.getString("message_type").equals("card_8")){
                    val dataJsonObject = message.getJSONObject("data")
                    val responseText = dataJsonObject.getJSONArray("response_text")
                    val stopTki = responseText.getJSONObject(0).getString("name")
                    Log.d("stopTki", stopTki.toString())
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    // Refresh the views
                    /*messageHolder.rlChipGroup.invalidate()
                    messageHolder.chipSideEffects.clearCheck()*/
                    // messageHolder.cvIssueDuration.invalidate()

                    messageHolder.tvLast48Hours.invalidate()
                    messageHolder.tvLastWeek.invalidate()
                    messageHolder.tvLastMonth.invalidate()
                    messageHolder.tvMoreThanAMonth.invalidate()

                    messageHolder.cvGreetings.visibility = View.GONE
                    messageHolder.cvSideEffectsIssue.visibility = View.GONE
                    messageHolder.chipSideEffects.visibility = View.GONE
                    messageHolder.cvIssueDuration.visibility = View.GONE
                    messageHolder.cvHelp.visibility = View.GONE
                    messageHolder.rlChipGroup.visibility = View.GONE
                    messageHolder.cvPossibleProblems.visibility = View.GONE
                    messageHolder.cvStopTki.visibility = View.VISIBLE
                    messageHolder.tvStopTki.text = stopTki.toString()
                    val time = message.getString("datetime")
                    var str1 = ""
                    if (time.isNotEmpty()) {
                        str1 = time
                    }
                    var splitTime = ""
                    if (str1.isNotEmpty()) {
                        splitTime = str1.split("\\s".toRegex())[1]
                        Log.d("splitTime", splitTime)
                    }
                    messageHolder.tvTkiTimeStamp.visibility = View.VISIBLE
                    messageHolder.tvTkiTimeStamp.text = splitTime

                    val option = dataJsonObject.getString("option")
                    Log.d("n_option",option)
                    // processingClick = false
                    if (!processingClick) {
                        if (option.isNotEmpty()) {
                            (context as ALKChatActivity).emitNextOption(
                                option
                            )
                            processingClick = true
                        }
                    }
                    /*var mOption = ""
                    if (mOption.isEmpty()) {
                        if (option.isNotEmpty()) {
                            (context as ALKChatActivity).emitNextOption(
                                option
                            )
                            mOption = option
                        }
                    }
                    if (mOption.isNotEmpty()){

                    }*/
                    /*if (option.isNotEmpty()) {
                        (context as ALKChatActivity).emitNextOption(
                            option
                        )
                    }*/
                } else if (message.getString("message_type").equals("not_understand")){
                    Log.d("not_understand", message.toString())
                    val notUnderstand = message.getString("message")
                    val messageHolder = holder as ReceivedMessageHolder
                    setFadeAnimation(messageHolder.itemView)
                    messageHolder.cvStopTki.visibility = View.VISIBLE
                    messageHolder.cvStopTki.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                    messageHolder.tvStopTki.setTextColor(Color.parseColor("#000000"))
                    messageHolder.tvStopTki.text = notUnderstand
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        vg.isEnabled = enable
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            child.isClickable = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }

    private fun addToList(newEntry: String?) {
        if (!mSideEffectsChipItems.contains(newEntry)) newEntry?.let { mSideEffectsChipItems.add(it) }
    }

    /*override fun getItemId(position: Int): Long {
        val product = messages[position]
        val dataJsonObject = product.getJSONObject("data")
        val responseId = dataJsonObject.getString("response_id")
        Log.d("check_data", responseId.toString())
        return product.get("").toString().toLong()
    }*/

    fun disableAllViews() {
        mHolder.tvSideEffects.isEnabled = false
        mHolder.tvSideEffects.isClickable = false
    }
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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(context, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(context, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }
}
