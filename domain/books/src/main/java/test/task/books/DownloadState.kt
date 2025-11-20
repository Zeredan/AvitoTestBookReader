package test.task.books

import java.io.File

sealed interface DownloadState {
    data object Started : DownloadState
    data class Progress(val progress: Int) : DownloadState
    data class Success(val file: File) : DownloadState
    data class Error(val throwable: Throwable) : DownloadState
}