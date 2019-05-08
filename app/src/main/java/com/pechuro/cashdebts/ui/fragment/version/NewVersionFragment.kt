package com.pechuro.cashdebts.ui.fragment.version

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_new_version.*

class NewVersionFragment : BaseFragment<NewVersionFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_new_version

    override fun getViewModelClass() = NewVersionFragmentViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
    }

    private fun setViewListeners() {
        button_download.setOnClickListener {
            openLink()
        }
    }

    private fun openLink() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(LINK))
        startActivity(browserIntent)
    }

    companion object {
        private const val LINK = "https://github.com/Ilyshka991/Cash_Debts/releases"

        fun newInstance() = NewVersionFragment()
    }
}

