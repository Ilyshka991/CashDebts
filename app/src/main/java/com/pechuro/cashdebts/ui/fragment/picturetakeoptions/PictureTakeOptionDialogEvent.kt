package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class PictureTakeOptionDialogEvent : BaseEvent() {
    object TakePictureFromCamera : PictureTakeOptionDialogEvent()
    object TakePictureFromGallery : PictureTakeOptionDialogEvent()
}