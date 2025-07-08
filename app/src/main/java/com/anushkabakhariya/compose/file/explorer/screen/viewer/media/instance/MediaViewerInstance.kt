package com.anushkabakhariya.compose.file.explorer.screen.viewer.media.instance

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.screen.viewer.ViewerInstance
import com.anushkabakhariya.compose.file.explorer.screen.viewer.media.misc.AudioPlayerManager
import com.anushkabakhariya.compose.file.explorer.screen.viewer.media.misc.MediaSource

class MediaViewerInstance(override val uri: Uri, override val id: String) : ViewerInstance {
    val player = ExoPlayer.Builder(globalClass).build()
    val mediaItem = MediaItem.fromUri(uri)
    val mediaSource = getMediaSource(globalClass, uri)

    val audioManager = AudioPlayerManager(globalClass)

    init {
        player.setMediaItem(mediaItem)
        player.prepare()

        if (mediaSource is MediaSource.AudioSource) {
            audioManager.prepare(uri)
        }
    }

    private fun getMediaSource(context: Context, uri: Uri): MediaSource {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: return MediaSource.UnknownSource

        return when {
            mimeType.startsWith("audio/") -> {
                MediaSource.AudioSource
            }

            mimeType.startsWith("video/") -> {
                MediaSource.VideoSource
            }

            else -> {
                MediaSource.UnknownSource
            }
        }
    }

    override fun onClose() {
        player.release()
        audioManager.release()
    }
}