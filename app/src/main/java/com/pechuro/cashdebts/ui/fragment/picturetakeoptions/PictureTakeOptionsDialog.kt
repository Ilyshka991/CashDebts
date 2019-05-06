package com.pechuro.cashdebts.ui.fragment.picturetakeoptions

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseBottomSheetDialog
import com.pechuro.cashdebts.ui.utils.EventManager
import kotlinx.android.synthetic.main.dialog_picture_take_options.*

class PictureTakeOptionsDialog : BaseBottomSheetDialog() {

    override val layoutId: Int
        get() = R.layout.dialog_picture_take_options

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        button_camera.setOnClickListener {
            EventManager.publish(AddOptionsEvent.TakePictureFromCamera)
            close()
        }
        button_gallery.setOnClickListener {
            EventManager.publish(AddOptionsEvent.TakePictureFromGallery)
            close()
        }
    }

    companion object {
        const val TAG = "add_option_dialog"

        fun newInstance() = PictureTakeOptionsDialog()
    }
}

