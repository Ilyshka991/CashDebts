package com.pechuro.cashdebts.data.data.structure

class FirestoreStructure {
    object RemoteDebt {
        const val TAG = "remoteDebts"

        object Structure {
            const val creditor = "creditorPhone"
            const val debtor = "debtorPhone"
            const val value = "value"
            const val description = "description"
            const val date = "date"
            const val status = "status"
        }
    }

    object LocalDebt {
        const val TAG = "localDebts"

        object Structure {
            const val ownerUid = "ownerUid"
            const val name = "name"
            const val value = "value"
            const val description = "description"
            const val date = "date"
            const val isPersonDebtor = "isOwnerDebtor"
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