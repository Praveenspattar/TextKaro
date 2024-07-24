package com.example.chatapp.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {
    private val _currentFragment = MutableLiveData<String>()
    val currentFragment: LiveData<String> get() = _currentFragment

    fun setCurrentFragment(fragmentName: String) {
        _currentFragment.value = fragmentName
    }
}