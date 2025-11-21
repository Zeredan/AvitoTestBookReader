package test.task.room

import test.task.books.Book
import test.task.books.BookFormat
import test.task.database.entities.BookEntity

fun BookEntity.toBook(): Book = Book(
    id = id,
    title = title,
    author = author,
    remoteUrl = remoteFileUrl,
    localPath = localFilePath,
    readProgress = readProgress,
    format = BookFormat.fromPath(localFilePath.toString()) ?: BookFormat.TXT,
)

fun Book.toBookEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author ?: "Неизвестный автор",
    remoteFileUrl = remoteUrl,
    localFilePath = localPath,
    readProgress = readProgress,
)