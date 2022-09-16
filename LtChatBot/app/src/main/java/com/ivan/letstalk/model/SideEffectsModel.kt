package com.ivan.letstalk.model

class SideEffectsModel(alkTitle: String?, alkDesc: String?, alkSymptoms: String?) {
    private var alkTitle: String
    private var alkDesc: String
    private var alkSymptoms: String

    init {
        this.alkTitle = alkTitle!!
        this.alkDesc = alkDesc!!
        this.alkSymptoms = alkSymptoms!!
    }

    fun getAlkTitle(): String? {
        return alkTitle
    }

    fun setTitle(name: String?) {
        alkTitle = name!!
    }

    fun getAlkDesc(): String? {
        return alkDesc
    }

    fun getAlkSymptoms(): String? {
        return alkSymptoms
    }
}