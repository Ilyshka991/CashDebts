package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.dialog_picture_take_options.view.*

class PictureTakeOptionsDialog : BottomSheetDialogFragment() {
    private lateinit var buttonCamera: Button
    private lateinit var buttonGallery: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_picture_take_options, container, false)
        buttonCamera = view.button_camera
        buttonGallery = view.button_gallery
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        buttonCamera.setOnClickListener {
            EventBus.publish(AddOptionsEvent.TakePictureFromCamera)
            dismiss()
        }
        buttonGallery.setOnClickListener {
            EventBus.publish(AddOptionsEvent.TakePictureFromGallery)
            dismiss()
        }
    }

    companion object {
        const val TAG = "add_option_dialog"

        fun newInstance() = PictureTakeOptionsDialog().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

sealed class AddOptionsEvent : BaseEvent() {
    object TakePictureFromCamera : AddOptionsEvent()
    object TakePictureFromGallery : AddOptionsEvent()
}