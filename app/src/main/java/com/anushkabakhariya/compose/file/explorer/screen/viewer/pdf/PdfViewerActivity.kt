package com.anushkabakhariya.compose.file.explorer.screen.viewer.pdf

import android.net.Uri
import android.util.Size
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anushkabakhariya.compose.file.explorer.App.Companion.globalClass
import com.anushkabakhariya.compose.file.explorer.common.extension.dp
import com.anushkabakhariya.compose.file.explorer.common.ui.SafeSurface
import com.anushkabakhariya.compose.file.explorer.screen.viewer.ViewerActivity
import com.anushkabakhariya.compose.file.explorer.screen.viewer.ViewerInstance
import com.anushkabakhariya.compose.file.explorer.screen.viewer.pdf.instance.PdfViewerInstance
import com.anushkabakhariya.compose.file.explorer.screen.viewer.pdf.ui.BottomBarView
import com.anushkabakhariya.compose.file.explorer.screen.viewer.pdf.ui.TopBarView
import com.anushkabakhariya.compose.file.explorer.theme.FileExplorerTheme
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarLayoutSide
import my.nanihadesuka.compose.ScrollbarSettings
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

class PdfViewerActivity : ViewerActivity() {
    override fun onCreateNewInstance(uri: Uri, uid: String): ViewerInstance {
        return PdfViewerInstance(uri, uid)
    }

    override fun onReady(instance: ViewerInstance) {
        if (instance is PdfViewerInstance) {
            setContent {
                FileExplorerTheme {
                    SafeSurface {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            var showToolbar by remember { mutableStateOf(false) }

                            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                                var pages by remember { mutableStateOf(emptyList<PdfViewerInstance.PdfPage>()) }
                                var pageSize = remember { Size(-1, -1) }

                                LaunchedEffect(Unit) {
                                    if (!instance.isLoaded) {
                                        instance.load(
                                            dimension = Size(
                                                constraints.maxWidth,
                                                constraints.maxHeight
                                            )
                                        )
                                    }

                                    pages = instance.pages
                                }

                                DisposableEffect(Unit) {
                                    onDispose {
                                        instance.onClose()
                                    }
                                }

                                val listState = rememberLazyListState()

                                LazyColumnScrollbar(
                                    state = listState,
                                    settings = ScrollbarSettings.Default.copy(
                                        thumbUnselectedColor = colorScheme.surfaceContainerHigh,
                                        thumbSelectedColor = colorScheme.surfaceContainerHighest,
                                        side = ScrollbarLayoutSide.Start
                                    ),
                                    indicatorContent = { index, isPressed ->
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = if (isPressed) colorScheme.surfaceContainerHighest else colorScheme.surfaceContainerHigh,
                                                    shape = RoundedCornerShape(
                                                        0.dp,
                                                        12.dp,
                                                        12.dp,
                                                        0.dp
                                                    )
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .zoomable(
                                                zoomState = rememberZoomState(),
                                                onTap = {
                                                    showToolbar = !showToolbar
                                                }
                                            ),
                                        state = listState,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        items(pages, key = { it.index }) { page ->
                                            DisposableEffect(Unit) {
                                                page.load()

                                                pageSize = Size(
                                                    page.dimension.width,
                                                    page.dimension.height
                                                )

                                                onDispose {
                                                    page.recycle(false)
                                                }
                                            }

                                            when (val pageContent = page.pageContent) {
                                                is PdfViewerInstance.PageContent.BlankPage -> {
                                                    val width =
                                                        if (page.isTemporaryPageSize && pageSize.width > 0)
                                                            pageSize.width.dp() else pageContent.width.dp()
                                                    val height =
                                                        if (page.isTemporaryPageSize && pageSize.height > 0)
                                                            pageSize.height.dp() else pageContent.height.dp()

                                                    Box(
                                                        modifier = Modifier
                                                            .size(width, height)
                                                            .background(color = Color.White)
                                                    )
                                                }

                                                is PdfViewerInstance.PageContent.BitmapPage -> {
                                                    Image(
                                                        modifier = Modifier.size(
                                                            pageContent.bitmap.width.dp(),
                                                            pageContent.bitmap.height.dp()
                                                        ),
                                                        bitmap = pageContent.bitmap.asImageBitmap(),
                                                        contentDescription = null,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = showToolbar,
                                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                            ) {
                                TopBarView(instance)
                            }

                            AnimatedVisibility(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                visible = showToolbar,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                BottomBarView(instance)
                            }
                        }
                    }
                }
            }
        } else {
            globalClass.showMsg("Invalid PDF")
            finish()
        }
    }
}