package com.pechuro.cashdebts.ui.fragment.progressdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pechuro.cashdebts.R

class ProgressDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            val size = resources.getDimensionPixelSize(R.dimen.progress_dialog_size)
            window?.setLayout(size, size)
            isCancelable = false
        }
    }

    companion object {
        const val TAG = "progressDialog"
        fun newInstance() = ProgressDialog()
    }
}