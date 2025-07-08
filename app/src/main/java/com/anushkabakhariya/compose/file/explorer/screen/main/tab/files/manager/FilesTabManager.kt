package com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.task.FilesTabTask
import com.anushkabakhariya.compose.file.explorer.screen.preferences.misc.prefMutableState

class FilesTabManager {
    val filesTabTasks = mutableStateListOf<FilesTabTask>()

    var bookmarks by prefMutableState(
        keyName = "bookmarks",
        defaultValue = emptySet(),
        getPreferencesKey = { stringSetPreferencesKey(it) }
    )
}