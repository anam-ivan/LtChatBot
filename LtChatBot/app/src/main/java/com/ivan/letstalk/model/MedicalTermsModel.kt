package com.ivan.letstalk.model

class MedicalTermsModel(title: String?, desc: String?) {
    private var title: String
    private var desc: String

    init {
        this.title = title!!
        this.desc = desc!!
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(name: String?) {
        title = name!!
    }

    fun getDesc(): String? {
        return desc
    }

    fun setDesc(n: String?) {
        desc = desc!!
    }

}