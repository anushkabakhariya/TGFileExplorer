package com.anushkabakhariya.compose.file.explorer.screen.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.SdStorage
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anushkabakhariya.compose.file.explorer.common.extension.toFormattedSize
import com.anushkabakhariya.compose.file.explorer.common.ui.Space
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder.StorageDeviceHolder
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.misc.INTERNAL_STORAGE
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.misc.ROOT

@Composable
fun StorageDeviceView(
    storageDeviceHolder: StorageDeviceHolder,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(12.dp)
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = when (storageDeviceHolder.type) {
                INTERNAL_STORAGE -> Icons.Rounded.FolderOpen
                ROOT -> Icons.Rounded.Numbers
                else -> Icons.Rounded.SdStorage
            },
            contentDescription = null
        )
        Space(size = 8.dp)
        Column {
            Text(text = storageDeviceHolder.title)
            Space(size = 8.dp)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    progress = { storageDeviceHolder.usedSize.toFloat() / storageDeviceHolder.totalSize },
                    strokeCap = StrokeCap.Round
                )
                Space(size = 8.dp)
                Text(
                    modifier = Modifier
                        .alpha(0.6f)
                        .weight(1f),
                    text = "${storageDeviceHolder.usedSize.toFormattedSize()}/${storageDeviceHolder.totalSize.toFormattedSize()}",
                    fontSize = 12.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}