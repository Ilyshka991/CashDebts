package com.pechuro.cashdebts.ui.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment(), HasSupportFragmentInjector {
    @Inject
    protected lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: V
    protected open val isViewModelShared
        get() = false

    @get:LayoutRes
    protected abstract val layoutId: Int
    protected lateinit var viewDataBinding: T
    protected open val bindingVariables: Map<Int, Any>? = null

    @Inject
    protected lateinit var weakCompositeDisposable: CompositeDisposable
    @Inject
    protected lateinit var strongCompositeDisposable: CompositeDisposable

    protected abstract fun getViewModelClass(): KClass<V>

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        initViewModel()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindingVariables?.forEach { (variable, obj) -> viewDataBinding.setVariable(variable, obj) }
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.executePendingBindings()
    }

    override fun onStop() {
        super.onStop()
        weakCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        strongCompositeDisposable.clear()
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    private fun performDI() = AndroidSupportInjection.inject(this)

    private fun initViewModel() {
        viewModel = if (isViewModelShared) {
            ViewModelProviders.of(requireActivity(), viewModelFactory).get(getViewModelClass().java)
        } else {
            ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass().java)
        }
    }
}