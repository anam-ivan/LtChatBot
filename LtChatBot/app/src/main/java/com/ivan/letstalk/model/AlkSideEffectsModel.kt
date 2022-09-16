package com.ivan.letstalk.model

class AlkSideEffectsModel(title: String?) {
    private var title: String

    init {
        this.title = title!!
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(name: String?) {
        title = name!!
    }
}