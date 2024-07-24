package com.example.chatapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
}
