package com.pechuro.cashdebts.ui.fragment.countryselection

import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.utils.BaseEvent

sealed class CountrySelectionFragmentEvent : BaseEvent() {
    class OnCountrySelect(val country: CountryData) : CountrySelectionFragmentEvent()
}