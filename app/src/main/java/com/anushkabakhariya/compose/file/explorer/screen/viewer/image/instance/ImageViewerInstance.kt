package com.anushkabakhariya.compose.file.explorer.screen.viewer.image.instance

import android.net.Uri
import com.anushkabakhariya.compose.file.explorer.screen.viewer.ViewerInstance

class ImageViewerInstance(override val uri: Uri, override val id: String) : ViewerInstance {
    override fun onClose() {

    }
}