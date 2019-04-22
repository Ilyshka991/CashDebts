package com.pechuro.cashdebts.ui.fragment.remotedebtlist

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
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.ItemTouchHelper
import com.pechuro.cashdebts.ui.fragment.debtuserprofile.DebtUserProfileDialog
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtItemSwipeCallback
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter.RemoteDebtListAdapter
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_remote_debt_list.*
import javax.inject.Inject

class RemoteDebtListFragment : BaseFragment<RemoteDebtListFragmentViewModel>() {
    @Inject
    lateinit var adapter: RemoteDebtListAdapter
    @Inject
    protected lateinit var layoutManager: RecyclerView.LayoutManager
    @Inject
    protected lateinit var swipeHelper: ItemTouchHelper<RemoteDebtItemSwipeCallback.SwipeAction>

    override val layoutId: Int
        get() = R.layout.fragment_remote_debt_list

    override fun getViewModelClass() = RemoteDebtListFragmentViewModel::class

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
        fab_add.show()
        recycler.apply {
            adapter = this@RemoteDebtListFragment.adapter
            layoutManager = this@RemoteDebtListFragment.layoutManager
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            setHasFixedSize(true)
        }
        swipeHelper.attachToRecyclerView(recycler)
        progress.isVisible = true
    }

    private fun setViewListeners() {
        fab_add.setOnClickListener {
            EventBus.publish(MainActivityEvent.OpenAddActivity(false))
        }
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) fab_add.hide() else fab_add.show()
            }
        })

        swipeHelper.actionEmitter.subscribe {

            //Fixme: find better solution for move items back
            swipeHelper.attachToRecyclerView(null)
            swipeHelper.attachToRecyclerView(recycler)

            when (it) {
                is RemoteDebtItemSwipeCallback.SwipeAction.Complete -> completeDebt(it.position)
                is RemoteDebtItemSwipeCallback.SwipeAction.Edit -> editDebt(it.position)
            }
        }.addTo(strongCompositeDisposable)

        adapter.longClickEmitter.subscribe(::showProfileDialog).addTo(strongCompositeDisposable)
        adapter.actionsClickEmitter.subscribe(viewModel::update).addTo(strongCompositeDisposable)
    }

    private fun setEventListeners() {
        EventBus.listen(AddDebtEvent::class.java).subscribe {
            when (it) {
                is AddDebtEvent.OnSuccess -> showSnackbarWithDelay(R.string.msg_success)
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            weakCompositeDisposable.addAll(
                debtSource.subscribe {
                    adapter.update(it)
                    if (progress.isVisible) progress.isVisible = false
                },
                viewModel.command.subscribe {
                    when (it) {
                        is RemoteDebtListFragmentViewModel.Command.ShowMessage -> showSnackbar(it.msgId)
                        is RemoteDebtListFragmentViewModel.Command.ShowUndoDeletionSnackbar -> showUndoDeletionSnackbar()
                    }
                }
            )
        }
    }

    private fun showUndoDeletionSnackbar() {
        Snackbar.make(coordinator, R.string.msg_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) {
                viewModel.restoreDebt()
            }
            .show()
    }

    private fun completeDebt(position: Int) {
        val item = adapter.getItemByPosition(position)
        viewModel.complete(item)
    }

    private fun editDebt(position: Int) {
        val item = adapter.getItemByPosition(position)
        EventBus.publish(MainActivityEvent.OpenAddActivity(false, item.id))
    }

    private fun showProfileDialog(user: RemoteDebt.User) {
        DebtUserProfileDialog.newInstance(user)
            .show(childFragmentManager, DebtUserProfileDialog.TAG)
    }

    private fun showSnackbar(@StringRes msgId: Int) {
        Snackbar.make(coordinator, msgId, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSnackbarWithDelay(@StringRes msgId: Int) {
        coordinator.postDelayed(
            { Snackbar.make(coordinator, msgId, Snackbar.LENGTH_SHORT).show() },
            SNACKBAR_SHOW_DELAY
        )
    }

    companion object {
        private const val SNACKBAR_SHOW_DELAY = 250L

        fun newInstance() = RemoteDebtListFragment()
    }
}