package com.anushkabakhariya.compose.file.explorer.screen.textEditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.R
import com.anushkabakhariya.compose.file.explorer.common.ui.InputDialog
import com.anushkabakhariya.compose.file.explorer.common.ui.InputDialogButton
import com.anushkabakhariya.compose.file.explorer.common.ui.InputDialogInput
import io.github.rosemoe.sora.widget.CodeEditor

@Composable
fun JumpToPositionDialog(codeEditor: CodeEditor) {
    val textEditorManager = globalClass.textEditorManager
    if (textEditorManager.showJumpToPositionDialog) {
        InputDialog(
            title = stringResource(R.string.jump_to_position),
            inputs = arrayListOf(
                InputDialogInput(stringResource(R.string.jump_to_position_label))
            ),
            buttons = arrayListOf(
                InputDialogButton(stringResource(R.string.go)) { inputs ->
                    val text = inputs[0].content
                    val position = textEditorManager.parseCursorPosition(text)
                    if (position.first > -1) {
                        runCatching {
                            codeEditor.setSelection(position.first - 1, position.second - 1)
                        }.exceptionOrNull()?.let {
                            globalClass.showMsg(R.string.invalid_position)
                        }
                    } else {
                        globalClass.showMsg(R.string.invalid_position)
                    }
                    textEditorManager.showJumpToPositionDialog = false
                }
            )
        ) { textEditorManager.showJumpToPositionDialog = false }
    }
}