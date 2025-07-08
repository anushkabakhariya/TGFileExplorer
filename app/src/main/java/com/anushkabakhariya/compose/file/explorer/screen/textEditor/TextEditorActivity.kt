package com.anushkabakhariya.compose.file.explorer.screen.textEditor

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.R
import com.anushkabakhariya.compose.file.explorer.base.BaseActivity
import com.anushkabakhariya.compose.file.explorer.common.extension.setContent
import com.anushkabakhariya.compose.file.explorer.common.ui.SafeSurface
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.BottomBarView
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.CodeEditorView
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.InfoBar
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.JumpToPositionDialog
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.RecentFilesDialog
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.SearchPanel
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.ToolbarView
import com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui.WarningDialog
import com.anushkabakhariya.compose.file.explorer.theme.FileExplorerTheme
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor

class TextEditorActivity : BaseActivity() {
    private val textEditorManager = globalClass.textEditorManager
    private lateinit var codeEditor: CodeEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()
    }

    override fun onPermissionGranted() {
        codeEditor = textEditorManager.createCodeEditorView(this)

        textEditorManager.readActiveFileContent { content, newText, isSourceChanged ->
            codeEditor.setContent(content, textEditorManager.getFileInstance()!!)

            if (isSourceChanged) {
                textEditorManager.showSourceFileWarningDialog {
                    codeEditor.setContent(Content(newText), textEditorManager.getFileInstance()!!)
                }
            }
        }

        textEditorManager.updateSymbols()

        setContent {
            FileExplorerTheme {
                SafeSurface {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BackHandler(enabled = textEditorManager.showSearchPanel) {
                            textEditorManager.hideSearchPanel(codeEditor)
                        }

                        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
                            textEditorManager.checkActiveFileValidity(
                                onSourceReload = {
                                    codeEditor.setContent(
                                        Content(it),
                                        textEditorManager.getFileInstance()!!
                                    )
                                },
                                onFileNotFound = {
                                    textEditorManager.getFileInstance()?.let {
                                        textEditorManager.fileInstanceList.remove(it)
                                    }
                                    globalClass.showMsg(getString(R.string.file_no_longer_exists))
                                    finish()
                                }
                            )
                        }

                        WarningDialog()
                        JumpToPositionDialog(codeEditor)
                        RecentFilesDialog(codeEditor)
                        ToolbarView(codeEditor, onBackPressedDispatcher)
                        HorizontalDivider()
                        InfoBar()
                        CodeEditorView(codeEditor)
                        HorizontalDivider()
                        BottomBarView(codeEditor)
                        SearchPanel(codeEditor)
                    }
                }
            }
        }
    }
}