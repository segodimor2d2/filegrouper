package com.reccode.filegrouper.util

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun loadMdFiles(
    context: Context,
    folderUri: Uri,
    onResult: (List<Triple<String, Uri, String>>) -> Unit
) {
    val contentResolver = context.contentResolver

    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
        folderUri,
        DocumentsContract.getTreeDocumentId(folderUri)
    )

    val mdFiles = mutableListOf<Triple<String, Uri, String>>()

    val projection = arrayOf(
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        DocumentsContract.Document.COLUMN_MIME_TYPE,
        DocumentsContract.Document.COLUMN_DOCUMENT_ID
    )

    contentResolver.query(childrenUri, projection, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
        val mimeIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE)
        val docIdIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID)

        while (cursor.moveToNext()) {
            val name = cursor.getString(nameIndex)
            val mimeType = cursor.getString(mimeIndex)
            val documentId = cursor.getString(docIdIndex)

            val fileUri = DocumentsContract.buildDocumentUriUsingTree(folderUri, documentId)

            mdFiles.add(Triple(name, fileUri, mimeType))
        }
    }

    onResult(mdFiles)
}

fun createNewMarkdownFile(context: Context): File {
    val formatter = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
    val fileName = formatter.format(Date()) + ".md"
    val dir = File(context.filesDir, "markdown_files")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, fileName)
    file.writeText("# Novo Arquivo Markdown\n")
    return file
}
