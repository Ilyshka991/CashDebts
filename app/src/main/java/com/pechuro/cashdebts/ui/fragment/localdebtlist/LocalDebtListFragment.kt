package com.pechuro.cashdebts.ui.fragment.localdebtlist

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.activity.adddebt.AddDebtEvent
import com.pechuro.cashdebts.ui.activity.main.MainActivityEvent
import com.pechuro.cashdebts.ui.activity.main.SnackActionInfo
import com.pechuro.cashdebts.ui.activity.main.SnackInfo
import com.pechuro.cashdebts.ui.activity.main.SnackbarManager
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.ItemTouchHelper
import com.pechuro.cashdebts.ui.fragment.filterdialog.FilterEvent
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtItemSwipeCallback
import com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter.LocalDebtListAdapter
import com.pechuro.cashdebts.ui.utils.EventManager
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_debt_list.*
import javax.inject.Inject

class LocalDebtListFragment : BaseFragment<LocalDebtListFragmentViewModel>() {
    @Inject
    protected lateinit var adapter: LocalDebtListAdapter
    @Inject
    protected lateinit var layoutManager: RecyclerView.LayoutManager
    @Inject
    protected lateinit var swipeHelper: ItemTouchHelper<LocalDebtItemSwipeCallback.SwipeAction>

    override val layoutId: Int
        get() = R.layout.fragment_debt_list

    override fun getViewModelClass() = LocalDebtListFragmentViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setViewListeners()
        setEventListeners()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    private fun setupView() {
        recycler.apply {
            adapter = this@LocalDebtListFragment.adapter
            layoutManager = this@LocalDebtListFragment.layoutManager
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
        swipeHelper.attachToRecyclerView(recycler)
        progress.isVisible = true
    }

    private fun setViewListeners() {
        swipeHelper.actionEmitter.subscribe {
            when (it) {
                is LocalDebtItemSwipeCallback.SwipeAction.Delete -> deleteDebt(it.position)
                is LocalDebtItemSwipeCallback.SwipeAction.Edit -> {
                    editDebt(it.position)
                    swipeHelper.attachToRecyclerView(null)
                    swipeHelper.attachToRecyclerView(recycler)
                }
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun setEventListeners() {
        strongCompositeDisposable.addAll(
            EventManager.listen(AddDebtEvent::class.java).subscribe {
                when (it) {
                    is AddDebtEvent.OnSuccess -> showSnackbar(R.string.msg_success)
                }
            }, EventManager.listen(FilterEvent::class.java).subscribe {
                when (it) {
                    is FilterEvent.OnChange -> {
                        weakCompositeDisposable.clear()
                        viewModel.initSource()
                        setViewModelListeners()
                    }
                }
            }
        )
    }

    private fun setViewModelListeners() {
        viewModel.debtSource.subscribe {
            adapter.update(it.diffResult)
            if (progress.isVisible) progress.isVisible = false
            updateTotalSum(it.totalSum)
        }.addTo(weakCompositeDisposable)
    }

    private fun updateTotalSum(totalSum: Double) {
        EventManager.publish(MainActivityEvent.UpdateTotalDebtSum(totalSum))
    }

    private fun deleteDebt(position: Int) {
        showUndoDeletionSnackbar()
        val item = adapter.getItemByPosition(position)
        viewModel.deleteDebt(item)
    }

    private fun editDebt(position: Int) {
        val item = adapter.getItemByPosition(position)
        EventManager.publish(MainActivityEvent.OpenAddActivity(true, item.id))
    }

    private fun showSnackbar(@StringRes msgId: Int) {
        view?.postDelayed({
            SnackbarManager.show(SnackInfo(msgId, Snackbar.LENGTH_SHORT))
        }, SNACKBAR_SHOW_DELAY)
    }

    private fun showUndoDeletionSnackbar() {
        SnackbarManager.show(
            SnackInfo(
                R.string.msg_deleted,
                Snackbar.LENGTH_LONG,
                SnackActionInfo(R.string.action_undo, viewModel::restoreDebt)
            )
        )
    }

    companion object {
        private const val SNACKBAR_SHOW_DELAY = 250L

        fun newInstance() = LocalDebtListFragment()
    }
}