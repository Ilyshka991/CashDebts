package com.pechuro.cashdebts.data

class FirestoreStructure {
    object Debts {
        const val TAG = "debts"

        object Structure {
            const val creditor = "creditorPhone"
            const val debtor = "debtorPhone"
            const val value = "value"
            const val description = "description"
            const val date = "date"
            const val status = "status"
        }
    }
}