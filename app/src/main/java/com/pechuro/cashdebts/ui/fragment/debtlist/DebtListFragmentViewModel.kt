package com.pechuro.cashdebts.ui.fragment.debtlist

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DebtListFragmentViewModel @Inject constructor() : BaseViewModel() {
    val data = BehaviorSubject.create<List<String>>()

    private val db = FirebaseFirestore.getInstance()

    init {
        getData()
    }

    fun add() {
        db.collection("debts").add(Debt("+375259050135", "AAfsdfsdf"))
            .addOnSuccessListener {

            }
    }

    private fun getData() {
        println(FirebaseAuth.getInstance().currentUser?.phoneNumber)
        db.collection("debts")
            .whereEqualTo("number", "+375259050135")
            .addSnapshotListener { value, e ->
                val debts = mutableListOf<String>()
                for (debt in value!!) {
                    debts += debt?.getString("name")!!
                }
                this.data.onNext(debts)
            }

    }
}