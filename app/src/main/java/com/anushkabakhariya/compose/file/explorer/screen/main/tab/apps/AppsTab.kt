package com.anushkabakhariya.compose.file.explorer.screen.main.tab.apps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.R
import com.anushkabakhariya.compose.file.explorer.common.extension.emptyString
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.Tab
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.apps.holder.AppHolder
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.apps.provider.getInstalledApps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsTab : Tab() {
    override val id = globalClass.generateUid()

    override val title = globalClass.getString(R.string.apps_tab_title)

    override val subtitle = emptyString

    override val header = globalClass.getString(R.string.apps_tab_header)

    val appsList = mutableStateListOf<AppHolder>()
    val systemApps = ArrayList<AppHolder>()
    val userApps = ArrayList<AppHolder>()

    var selectedChoice by mutableIntStateOf(0)
    var previewAppDialog by mutableStateOf<AppHolder?>(null)
    var isSearchPanelOpen by mutableStateOf(false)
    var searchQuery by mutableStateOf(emptyString)
    var isSearching by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    override fun onTabResumed() {
        super.onTabResumed()
        requestHomeToolbarUpdate()
    }

    override fun onTabStarted() {
        super.onTabStarted()
        requestHomeToolbarUpdate()
    }

    override fun onBackPressed(): Boolean {
        if (isSearchPanelOpen || isSearching || isLoading) {
            if (isSearching) {
                isSearching = false
            }

            if (isSearchPanelOpen) {
                isSearchPanelOpen = false
            }

            appsList.clear()

            when (selectedChoice) {
                0 -> appsList.addAll(userApps)
                1 -> appsList.addAll(systemApps)
                2 -> {
                    appsList.addAll(userApps)
                    appsList.addAll(systemApps)
                }
            }
            return true
        }
        return false
    }

    fun fetchInstalledApps() {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            getInstalledApps(globalClass).forEach {
                if (it.isSystemApp) systemApps.add(it)
                else userApps.add(it)
            }

            appsList.clear()
            appsList.addAll(userApps)

            isLoading = false
        }
    }
}