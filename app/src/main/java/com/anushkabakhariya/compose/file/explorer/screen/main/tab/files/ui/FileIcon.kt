package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder.DocumentHolder
import com.anushkabakhariya.compose.file.explorer.screen.preferences.constant.FilesTabFileListSize
import com.anushkabakhariya.compose.file.explorer.screen.preferences.constant.FilesTabFileListSizeMap

@Composable
fun FileIcon(
    documentHolder: DocumentHolder,
    onClickListener: (() -> Unit)? = null
) {
    val preferencesManager = globalClass.preferencesManager

    val iconSize = when (preferencesManager.displayPrefs.fileListSize) {
        FilesTabFileListSize.SMALL.ordinal -> FilesTabFileListSizeMap.IconSize.SMALL.dp
        FilesTabFileListSize.MEDIUM.ordinal -> FilesTabFileListSizeMap.IconSize.MEDIUM.dp
        FilesTabFileListSize.LARGE.ordinal -> FilesTabFileListSizeMap.IconSize.LARGE.dp
        else -> FilesTabFileListSizeMap.IconSize.EXTRA_LARGE.dp
    }

    if (documentHolder.isFile) {
        ItemRowIcon(
            icon = documentHolder,
            alpha = if (documentHolder.isHidden) 0.4f else 1f,
            onClickListener = onClickListener,
            placeholder = documentHolder.getFileIconResource()
        )
    } else {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    onClickListener?.invoke()
                }
                .alpha(if (documentHolder.isHidden) 0.4f else 1f),
            imageVector = Icons.Rounded.Folder,
            contentDescription = null
        )
    }
}

@SuppressLint("CheckResult", "UseCompatLoadingForDrawables")
@Composable
fun ItemRowIcon(
    icon: Any?,
    alpha: Float = 1f,
    onClickListener: (() -> Unit)? = null,
    placeholder: Int,
) {
    val preferencesManager = globalClass.preferencesManager

    val iconSize = when (preferencesManager.displayPrefs.fileListSize) {
        FilesTabFileListSize.SMALL.ordinal -> FilesTabFileListSizeMap.IconSize.SMALL.dp
        FilesTabFileListSize.MEDIUM.ordinal -> FilesTabFileListSizeMap.IconSize.MEDIUM.dp
        FilesTabFileListSize.LARGE.ordinal -> FilesTabFileListSizeMap.IconSize.LARGE.dp
        else -> FilesTabFileListSizeMap.IconSize.EXTRA_LARGE.dp
    }

    val modifier = Modifier
        .size(iconSize)
        .clip(RoundedCornerShape(4.dp))
        .then(if (onClickListener != null) Modifier.clickable { onClickListener() } else Modifier)

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(globalClass).data(icon).build(),
        filterQuality = FilterQuality.Low,
        error = painterResource(id = placeholder),
        contentScale = ContentScale.Fit,
        alpha = alpha,
        contentDescription = null
    )
}