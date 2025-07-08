package com.anushkabakhariya.compose.file.explorer.screen.preferences.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ManageSearch
import androidx.compose.material.icons.rounded.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.R

@Composable
fun GeneralContainer() {
    val manager = globalClass.preferencesManager
    val preferences = manager.generalPrefs

    val limits = arrayListOf(
        "15", "25", "50", "100", "Unlimited"
    )

    Container(title = stringResource(R.string.general)) {
        PreferenceItem(
            label = stringResource(R.string.search_in_files_limit),
            supportingText = if (preferences.searchInFilesLimit == -1) limits[4] else preferences.searchInFilesLimit.toString(),
            icon = Icons.AutoMirrored.Rounded.ManageSearch,
            onClick = {
                manager.singleChoiceDialog.show(
                    title = globalClass.getString(R.string.search_in_files_limit),
                    description = globalClass.getString(R.string.maximum_number_of_files_search_desc),
                    choices = limits,
                    selectedChoice = if (preferences.searchInFilesLimit == -1) 4 else limits.indexOf(
                        preferences.searchInFilesLimit.toString()
                    ),
                    onSelect = {
                        val limit = when (limits[it]) {
                            limits[4] -> -1
                            else -> limits[it].toIntOrNull() ?: -1
                        }
                        preferences.searchInFilesLimit = limit
                    }
                )
            }
        )

        PreferenceItem(
            label = stringResource(R.string.sign_apk),
            supportingText = globalClass.getString(R.string.sign_apk_desc),
            icon = Icons.Rounded.Android,
            switchState = preferences.signApk,
            onSwitchChange = { preferences.signApk = it }
        )
    }
}