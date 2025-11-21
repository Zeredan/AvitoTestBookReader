package test.task.datasources

import java.io.File
import java.io.InputStream

interface BooksLocalDataSource {
    suspend fun saveFile(inputStream: InputStream, fileName: String): File
    suspend fun deleteFile(filePath: String): Boolean
    suspend fun getFile(filePath: String): File?
    fun getBooksDirectory(): File
    suspend fun getFileSize(filePath: String): Long
}