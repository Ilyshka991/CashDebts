package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import javax.inject.Inject

class ProfileViewFragmentViewModel @Inject constructor(private val userRepository: IUserRepository) : BaseViewModel()