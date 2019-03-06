package com.pechuro.cashdebts.ui.activity.auth.countyselect

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentCountrySelectBinding
import com.pechuro.cashdebts.ui.activity.auth.AuthActivityViewModel
import com.pechuro.cashdebts.ui.base.BaseFragment
import javax.inject.Inject

class CountySelectFragment : BaseFragment<FragmentCountrySelectBinding, AuthActivityViewModel>() {
    @Inject
    protected lateinit var adapter: CountrySelectAdapter

    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.fragment_country_select

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupView()
    }

    private fun setupView() {
        viewDataBinding.recycler.apply {
            adapter = this@CountySelectFragment.adapter
        }
    }


    companion object {

        fun newInstance() = CountySelectFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}