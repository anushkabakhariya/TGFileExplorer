package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder

import android.graphics.Bitmap
import com.anushkabakhariya.compose.file.explorer.common.extension.randomString

data class OpenWithActivityHolder(
    val id: String = String.randomString(8),
    val label: String,
    val name: String,
    val packageName: String,
    val icon: Bitmap?
)