package com.pechuro.cashdebts.ui.fragment.profileedit

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableField
import com.google.firebase.storage.FirebaseStorage
import com.pechuro.cashdebts.data.CurrentUser
import com.pechuro.cashdebts.data.FirebaseStorage.Companion.AVATARS_PATH
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.utils.AVATAR_PATH
import com.pechuro.cashdebts.utils.isExternalStorageWritable
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileEditFragmentViewModel @Inject constructor(
    private val currentUser: CurrentUser,
    private val storage: FirebaseStorage,
    private val appContext: Context
) : BaseViewModel() {

    val data = ProfileEditModel(currentUser.displayName, currentUser.photoUrl)
    val localAvatarUri = ObservableField<Uri?>()

    private var localAvatarFile: File? = null

    fun save() {
        if (!data.isValid()) return
        if (localAvatarFile != null) {
            val avatarRef = storage.reference.child("$AVATARS_PATH/${localAvatarFile!!.name}")
            val uri = Uri.fromFile(localAvatarFile)
            avatarRef.putFile(uri)
                .continueWithTask {
                    if (!it.isSuccessful) {
                        throw Exception("PZDC")
                    }
                    avatarRef.downloadUrl
                }
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        currentUser.update(data.getDisplayName(), it.result)
                    }
                }
        } else {
            currentUser.update(data.getDisplayName())
        }
    }

    fun loadTempImage() {
        localAvatarUri.set(localAvatarFile?.toUri())
    }

    fun createImageFile() = try {
        createAvatarFile(appContext, "${currentUser.phoneNumber!!.substring(1)}.jpg")?.also {
            localAvatarFile = it
        }
    } catch (ex: IOException) {
        null
    }

    @Throws(IOException::class)
    private fun createAvatarFile(context: Context?, name: String): File? {
        if (!isExternalStorageWritable()) {
            return null
        }
        val storageDir = context?.let { File(it.filesDir, AVATAR_PATH) } ?: return null
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        return File(storageDir, name)
    }
}