package com.pechuro.cashdebts.data.structure

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

    object Users {
        const val TAG = "users"

        object Structure {
            const val firstName = "firstName"
            const val lastName = "lastName"
            const val phoneNumber = "phoneNumber"
            const val photoUrl = "photoUrl"
        }
    }
}