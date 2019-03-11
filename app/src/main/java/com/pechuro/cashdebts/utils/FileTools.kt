package com.pechuro.cashdebts.utils

import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.InputStream

const val AVATAR_PATH = "Images"

fun isExternalStorageWritable() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

fun isExternalStorageReadable() = Environment.getExternalStorageState() in
        setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)

fun InputStream.getBytes(): ByteArray {
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