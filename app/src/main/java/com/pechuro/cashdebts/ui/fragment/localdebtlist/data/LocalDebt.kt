package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import com.pechuro.cashdebts.data.data.model.DebtRole
import java.util.*

data class LocalDebt(
    val id: String,
    val personName: String,
    val value: Double,
    val description: String,
    val date: Date,
    @DebtRole val role: Int,
    val isCompleted: Boolean,
    var isExpanded: Boolean = false
) {
    fun isEmpty() = personName.isEmpty() && value == 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalDebt

        if (id != other.id) return false
        if (personName != other.personName) return false
        if (value != other.value) return false
        if (description != other.description) return false
        if (date != other.date) return false
        if (role != other.role) return false
        if (isCompleted != other.isCompleted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + personName.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + role
        result = 31 * result + isCompleted.hashCode()
        return result
    }

    companion object {
        val EMPTY = LocalDebt("", "", 0.0, "", Date(), DebtRole.CREDITOR, false)
    }
}