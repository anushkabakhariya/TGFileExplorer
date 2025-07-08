package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.common.extension.emptyString
import com.anushkabakhariya.compose.file.explorer.common.ui.Space
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder.DocumentHolder
import com.anushkabakhariya.compose.file.explorer.screen.preferences.constant.FilesTabFileListSize
import com.anushkabakhariya.compose.file.explorer.screen.preferences.constant.FilesTabFileListSizeMap

@Composable
fun FileItemRow(
    item: DocumentHolder,
    fileDetails: String,
    namePrefix: String = emptyString,
    onFileIconClick: (() -> Unit)? = null
) {
    ItemRow(
        title = namePrefix + item.getName(),
        subtitle = fileDetails,
        icon = {
            FileIcon(
                documentHolder = item,
                onClickListener = onFileIconClick
            )
        }
    )
}

@Composable
fun ItemRow(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit = { },
    onItemClick: (() -> Unit)? = null,
) {
    val preferencesManager = globalClass.preferencesManager

    Column(
        Modifier
            .fillMaxWidth()
            .then(if (onItemClick != null) Modifier.clickable { onItemClick() } else Modifier)) {
        Space(
            size = when (preferencesManager.displayPrefs.fileListSize) {
                FilesTabFileListSize.LARGE.ordinal, FilesTabFileListSize.EXTRA_LARGE.ordinal -> 8.dp
                else -> 4.dp
            }
        )

        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Space(size = 8.dp)

            Column(
                Modifier.weight(1f)
            ) {
                val fontSize = when (preferencesManager.displayPrefs.fileListSize) {
                    FilesTabFileListSize.SMALL.ordinal -> FilesTabFileListSizeMap.FontSize.SMALL
                    FilesTabFileListSize.MEDIUM.ordinal -> FilesTabFileListSizeMap.FontSize.MEDIUM
                    FilesTabFileListSize.LARGE.ordinal -> FilesTabFileListSizeMap.FontSize.LARGE
                    else -> FilesTabFileListSizeMap.FontSize.EXTRA_LARGE
                }

                Text(
                    text = title,
                    fontSize = fontSize.sp,
                    maxLines = 1,
                    lineHeight = (fontSize + 2).sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = subtitle,
                    fontSize = (fontSize - 4).sp,
                    maxLines = 1,
                    lineHeight = (fontSize + 2).sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Space(
            size = when (preferencesManager.displayPrefs.fileListSize) {
                FilesTabFileListSize.LARGE.ordinal, FilesTabFileListSize.EXTRA_LARGE.ordinal -> 8.dp
                else -> 4.dp
            }
        )
    }


}