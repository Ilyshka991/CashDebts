package com.pechuro.cashdebts.model.files

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FileManager @Inject constructor(private val context: Context) {

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createAvatarFile(uid: String): File? {
        if (!isExternalStorageWritable()) {
            return null
        }
        val storageDir = File(context.filesDir, AVATAR_PATH)
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Date())
        return File.createTempFile("JPEG_${uid}_${timeStamp}_", ".jpg", storageDir)
    }

    fun writeToFile(file: File, inputStream: InputStream) {
        file.writeBytes(inputStream.getBytes())
    }

    private fun isExternalStorageWritable() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun InputStream.getBytes(): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        var len: Int
        len = read(buffer)
        while (len != -1) {
            byteBuffer.write(buffer, 0, len)
            len = read(buffer)
        }
        return byteBuffer.toByteArray()
    }

    companion object {
        private const val AVATAR_PATH = "Images"
    }
}