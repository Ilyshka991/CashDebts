package com.pechuro.cashdebts.ui.base

import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityContainerBinding
import com.pechuro.cashdebts.ui.base.base.BaseViewModel

abstract class ContainerBaseActivity<VM : BaseViewModel> : BaseFragmentActivity<ActivityContainerBinding, VM>() {

    override val layoutId: Int
        get() = R.layout.activity_container
    override val cointainerId: Int
        get() = viewDataBinding.container.id
}
