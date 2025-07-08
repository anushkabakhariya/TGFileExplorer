package com.anushkabakhariya.compose.file.explorer.screen.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.base.BaseActivity
import com.anushkabakhariya.compose.file.explorer.common.ui.SafeSurface
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.files.holder.DocumentHolder
import com.anushkabakhariya.compose.file.explorer.screen.main.tab.home.HomeTab
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.AppInfoDialog
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.JumpToPathDialog
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.SaveTextEditorFilesDialog
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.TabContentView
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.TabLayout
import com.anushkabakhariya.compose.file.explorer.screen.main.ui.Toolbar
import com.anushkabakhariya.compose.file.explorer.theme.FileExplorerTheme
import java.io.File

class MainActivity : BaseActivity() {
    private val HOME_SCREEN_SHORTCUT_EXTRA_KEY = "filePath"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()
    }

    override fun onPermissionGranted() {
        setContent {
            FileExplorerTheme {
                SafeSurface {
                    val coroutineScope = rememberCoroutineScope()
                    val mainActivityManager = globalClass.mainActivityManager
                    val pagerState =
                        rememberPagerState(initialPage = mainActivityManager.selectedTabIndex) {
                            mainActivityManager.tabs.size
                        }

                    BackHandler {
                        if (mainActivityManager.canExit(coroutineScope)) {
                            finish()
                        }
                    }

                    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                        if (mainActivityManager.tabs.isNotEmpty())
                            mainActivityManager.tabs[mainActivityManager.selectedTabIndex].onTabResumed()
                    }

                    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
                        if (mainActivityManager.tabs.isNotEmpty())
                            mainActivityManager.tabs[mainActivityManager.selectedTabIndex].onTabStopped()
                    }

                    LaunchedEffect(mainActivityManager.selectedTabIndex) {
                        if (mainActivityManager.tabs.isEmpty()) {
                            mainActivityManager.addTabAndSelect(
                                HomeTab()
                            )
                        }
                        handleIntent()

                        if (pagerState.currentPage != mainActivityManager.selectedTabIndex) {
                            pagerState.scrollToPage(mainActivityManager.selectedTabIndex)
                        }
                    }

                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            if (page != mainActivityManager.selectedTabIndex) {
                                mainActivityManager.selectTabAt(page)
                            }
                        }
                    }

                    JumpToPathDialog()

                    AppInfoDialog()

                    SaveTextEditorFilesDialog { finish() }

                    Column(Modifier.fillMaxSize()) {
                        Toolbar()
                        TabLayout()
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f),
                            key = { mainActivityManager.tabs[it].id }
                        ) { index ->
                            key(index) {
                                TabContentView(index)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleIntent() {
        intent?.let {
            if (it.hasExtra(HOME_SCREEN_SHORTCUT_EXTRA_KEY)) {
                globalClass.mainActivityManager.jumpToFile(
                    DocumentHolder.fromFile(File(it.getStringExtra(HOME_SCREEN_SHORTCUT_EXTRA_KEY)!!)),
                    this
                )
                intent = null
            }
        }
    }
}