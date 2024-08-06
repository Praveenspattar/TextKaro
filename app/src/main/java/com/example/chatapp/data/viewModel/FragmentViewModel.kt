package com.example.chatapp.data.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {
    private val _currentFragment = MutableLiveData<Pair<String,Bundle?>>()
    val currentFragment: LiveData<Pair<String,Bundle?>> get() = _currentFragment

    fun setCurrentFragment(fragmentName: String, args: Bundle? = null) {
        _currentFragment.value = Pair(fragmentName, args)
    }
}