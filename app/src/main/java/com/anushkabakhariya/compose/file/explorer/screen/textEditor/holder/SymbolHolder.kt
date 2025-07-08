package com.anushkabakhariya.compose.file.explorer.screen.textEditor.holder

import com.anushkabakhariya.compose.file.explorer.common.extension.emptyString

data class SymbolHolder(
    var label: String,
    var onClick: String = label,
    var onClickLength: Int = -1,
    var onLongClick: String = emptyString,
    var onLongClickLength: Int = -1,
    var onSelectionStart: String = emptyString,
    var onSelectionEnd: String = emptyString,
)