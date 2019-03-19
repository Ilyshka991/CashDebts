package com.pechuro.cashdebts.ui.base.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    HasSupportFragmentInjector {
    @Inject
    protected lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: V

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
        performDataBinding()
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

    private fun performDI() = AndroidInjection.inject(this)

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass().java)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        bindingVariables?.forEach { (variable, obj) -> viewDataBinding.setVariable(variable, obj) }
        viewDataBinding.executePendingBindings()
    }
}