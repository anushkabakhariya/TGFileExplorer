package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.FilesTab

@Composable
fun ColumnScope.FilesTabContentView(tab: FilesTab) {
    ApkPreviewDialog(tab)
    OpenWithAppListDialog(tab)
    BookmarksDialog(tab)
    SearchDialog(tab)
    TaskPanel(tab)
    TaskDialog(tab)
    RenameFileDialog(tab)
    DeleteConfirmationDialog(tab)
    CreateNewFileDialog(tab)
    FileCompressionDialog(tab)
    FileOptionsMenuDialog(tab)
    FilePropertiesDialog(tab)
    PathListRow(tab)
    HorizontalDivider(modifier = Modifier, thickness = 1.dp)
    FilesList(tab)
    BottomOptionsBar(tab)
}