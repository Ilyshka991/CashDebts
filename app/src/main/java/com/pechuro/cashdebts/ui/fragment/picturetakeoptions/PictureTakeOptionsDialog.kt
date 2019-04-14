package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.dialog_picture_take_options.*

class PictureTakeOptionsDialog : BottomSheetDialogFragment() {
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_picture_take_options, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        button_camera.setOnClickListener {
            EventBus.publish(AddOptionsEvent.TakePictureFromCamera)
            close()
        }
        button_gallery.setOnClickListener {
            EventBus.publish(AddOptionsEvent.TakePictureFromGallery)
            close()
        }
    }

    private fun close() {
        handler.post { }
        dismiss()
    }

    companion object {
        const val TAG = "add_option_dialog"

        fun newInstance() = PictureTakeOptionsDialog()
    }
}

sealed class AddOptionsEvent : BaseEvent() {
    object TakePictureFromCamera : AddOptionsEvent()
    object TakePictureFromGallery : AddOptionsEvent()
}