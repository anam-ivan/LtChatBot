package com.ivan.letstalk.helper

import java.io.FileFilter
import java.util.*

object CheckImageExtension {
    /** The is valid image.  */
    var isValidImage = FileFilter { pathname ->
        val name = pathname.name
        var ext: String? = null
        val i = name.lastIndexOf('.')
        if (i > 0 && i < name.length - 1) {
            ext = name.substring(i + 1).lowercase(Locale.getDefault())
        }
        if (ext == null) false else !(ext != "jpg" && ext != "jpeg" && ext != "png" && ext != "gif")
    }
}