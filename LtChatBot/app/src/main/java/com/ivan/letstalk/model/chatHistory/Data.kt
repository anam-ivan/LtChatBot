package com.ivan.letstalk.model.chatHistory

import com.google.gson.annotations.SerializedName


data class Data(
  @SerializedName("chat_info") var chatInfo: ChatInfo?,
  @SerializedName("conversation") var conversation: ArrayList<Conversation> = arrayListOf()
)