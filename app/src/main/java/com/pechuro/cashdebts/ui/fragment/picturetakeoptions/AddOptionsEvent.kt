package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class AddOptionsEvent : BaseEvent() {
    object TakePictureFromCamera : AddOptionsEvent()
    object TakePictureFromGallery : AddOptionsEvent()
}