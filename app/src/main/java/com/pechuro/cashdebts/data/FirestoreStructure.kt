package com.pechuro.cashdebts.data

class FirestoreStructure {
    object Debts {
        const val TAG = "debts"

        object Structure {
            const val creditor = "creditor"
            const val debtor = "debtor"
            const val value = "value"
            const val description = "description"
            const val date = "date"
            const val status = "status"
        }
    }
}