package com.anushkabakhariya.compose.file.explorer.screen.main.tab.home.holder

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeCategoryHolder(
    val name: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)