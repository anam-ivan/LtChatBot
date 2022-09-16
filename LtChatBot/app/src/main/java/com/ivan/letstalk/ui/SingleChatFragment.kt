package com.ivan.letstalk.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.ivan.letstalk.R

class SingleChatFragment : BottomSheetDialogFragment() {
    private lateinit var startChat: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_single_chat, container, false)
        startChat = view.findViewById(R.id.btn_start_new_chat)
        startChat.setOnClickListener {
            val intent = Intent(requireActivity(), ALKChatActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}