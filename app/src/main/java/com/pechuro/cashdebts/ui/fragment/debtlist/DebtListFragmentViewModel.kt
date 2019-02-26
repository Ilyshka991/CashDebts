package com.pechuro.cashdebts.ui.fragment.debtlist

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.model.DatabaseStructure
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.data.user.User
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor() : BaseViewModel() {
    val dataOnce = BehaviorSubject.create<Debt>()
    val dataList = BehaviorSubject.create<List<Debt>>()

    private val db = FirebaseFirestore.getInstance()

    init {
        getData()
    }

    fun add() {
        //   db.collection("debts").add(Debt(userPhone, "+375111111111", 20.4, "Test"))
    }

    private fun getData() {
        Observable.create<DocumentSnapshot> {
            db.collection(DatabaseStructure.Users.TAG).document(User.currentUserPhone)
                .addSnapshotListener { snapshot, e ->
                    if (!it.isDisposed && snapshot != null) it.onNext(snapshot)
                }
        }
            .subscribeOn(Schedulers.io())
            .map { it.get(DatabaseStructure.Users.DEBTS) as? List<DocumentReference> }
            .flatMap {
                Observable.fromIterable(it)
                    .subscribeOn(Schedulers.io())
            }
            .doOnNext {
                it.addSnapshotListener { snapshot, exception ->
                    println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
                }
            }
            .map { Tasks.await(it.get()) }
            .map { it.getDebt() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("BBBBBBBBBBBBBBBBBBBBB")
                //  dataList.onNext(it)
            }, {

            })
            .let(compositeDisposable::add)


        /* db.collection(DatabaseStructure.Users.TAG).document(User.currentUserPhone)
             .addSnapshotListener { snapshot, e ->

                 (snapshot?.get(DatabaseStructure.Users.DEBTS) as? List<DocumentReference>)?.also { referenceList ->
                     Observable.fromIterable(referenceList)
                         .subscribeOn(Schedulers.io())
                         .doOnNext {
                             it.addSnapshotListener { snapshot, exception ->
                                 println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
                             }
                         }
                         .map { Tasks.await(it.get()) }
                         .map { it.getDebt() }
                         .toList()
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe({
                             dataList.onNext(it)
                         }, {})
                         .let(compositeDisposable::add)
                 }
             }*/
    }


    private fun DocumentSnapshot.getDebt() = Debt(
        id,
        getString("debtor")!!,
        getString("debtee")!!,
        getDouble("value")!!,
        getString("description")
    )
}


/* .addSnapshotListener { value, e ->
           val debts = mutableListOf<Debt>()
           value?.forEach {
               val debt = Debt(
                   it.getString("debtor")!!,
                   it.getString("debtee")!!,
                   it.getDouble("value")!!,
                   it.getString("description")!!
               )
               debts += debt
           }
           this.data.onNext(debts)
       }*/