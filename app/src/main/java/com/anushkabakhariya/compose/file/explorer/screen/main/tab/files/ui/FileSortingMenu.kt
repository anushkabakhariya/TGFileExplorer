package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.R
import com.anushkabakhariya.compose.file.explorer.common.ui.BottomSheetDialog
import com.anushkabakhariya.compose.file.explorer.common.ui.CheckableItem
import com.anushkabakhariya.compose.file.explorer.common.ui.Space
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.FilesTab
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.misc.FileSortingPrefs
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.misc.SortingMethod

@Composable
fun FileSortingMenu(
    tab: FilesTab,
    reloadFiles: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val prefs = globalClass.preferencesManager.filesSortingPrefs
    val specificOptions = prefs.getSortingPrefsFor(tab.activeFolder)

    var applyForThisFileOnly by remember(tab.activeFolder.path) {
        mutableStateOf(specificOptions.applyForThisFileOnly)
    }
    var sortingMethod by remember(tab.activeFolder.path) {
        mutableIntStateOf(specificOptions.sortMethod)
    }
    var showFoldersFirst by remember(tab.activeFolder.path) {
        mutableStateOf(specificOptions.showFoldersFirst)
    }
    var reverseOrder by remember(tab.activeFolder.path) {
        mutableStateOf(specificOptions.reverseSorting)
    }

    fun updateForThisFolder() {
        prefs.setSortingPrefsFor(
            tab.activeFolder,
            FileSortingPrefs(
                sortingMethod,
                showFoldersFirst,
                reverseOrder,
                true
            )
        )
    }

    BottomSheetDialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.sort_by),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Space(size = 12.dp)

        CheckableItem(
            text = stringResource(R.string.apply_to_this_folder_only),
            applyForThisFileOnly,
            icon = Icons.Rounded.ArrowDownward,
            onClick = {
                applyForThisFileOnly = !applyForThisFileOnly
            }
        )

        HorizontalDivider()

        CheckableItem(
            text = stringResource(R.string.name_a_z),
            sortingMethod == SortingMethod.SORT_BY_NAME,
            icon = Icons.Rounded.SortByAlpha,
            onClick = {
                sortingMethod = SortingMethod.SORT_BY_NAME
            }
        )

        CheckableItem(
            text = stringResource(R.string.date_newer),
            sortingMethod == SortingMethod.SORT_BY_DATE,
            icon = Icons.Rounded.DateRange,
            onClick = {
                sortingMethod = SortingMethod.SORT_BY_DATE
            }
        )

        CheckableItem(
            text = stringResource(R.string.size_smaller),
            sortingMethod == SortingMethod.SORT_BY_SIZE,
            icon = Icons.AutoMirrored.Rounded.Sort,
            onClick = {
                sortingMethod = SortingMethod.SORT_BY_SIZE
            }
        )

        HorizontalDivider()

        CheckableItem(
            text = stringResource(R.string.folders_first),
            showFoldersFirst,
            icon = Icons.Rounded.Folder,
            onClick = {
                showFoldersFirst = !showFoldersFirst
            }
        )

        CheckableItem(
            text = stringResource(R.string.reverse),
            reverseOrder,
            icon = Icons.AutoMirrored.Rounded.InsertDriveFile,
            onClick = {
                reverseOrder = !reverseOrder
            }
        )

        Space(8.dp)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                if (applyForThisFileOnly) {
                    updateForThisFolder()
                } else {
                    prefs.showFoldersFirst = showFoldersFirst
                    prefs.reverseFilesSortingMethod = reverseOrder
                    prefs.filesSortingMethod = sortingMethod
                }

                reloadFiles()

                onDismissRequest()
            }
        ) {
            Text(text = stringResource(R.string.apply))
        }

        Space(size = 16.dp)
    }
}