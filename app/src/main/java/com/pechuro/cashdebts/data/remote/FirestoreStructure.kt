package com.pechuro.cashdebts.data.remote

class FirestoreStructure {
    object Debts {
        const val TAG = "debts"

        object Structure {
            const val debtor = "debtor"
            const val debtee = "debtee"
            const val value = "value"
            const val description = "description"
        }
    }
}