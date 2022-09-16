package com.ivan.letstalk.model

class MyDocumentModel(docTitle: String?, docCategory: String?) {
    private var docTitle: String
    private var docCategory: String

    init {
        this.docTitle = docTitle!!
        this.docCategory = docCategory!!
    }

    fun getDocTitle(): String? {
        return docTitle
    }

    fun setDocTitle(name: String?) {
        docTitle = name!!
    }

    fun getDocCategory(): String? {
        return docCategory
    }

    fun setDocCategory(n: String?) {
        docCategory = n!!
    }
}