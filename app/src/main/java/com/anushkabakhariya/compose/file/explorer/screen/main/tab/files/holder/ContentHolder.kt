package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder

import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass

abstract class ContentHolder {
    val uid = globalClass.generateUid()

    abstract fun getName(): String
    abstract fun getContent(): Any
}