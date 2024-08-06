package com.example.chatapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) {
    if (bundle != null) {
        fragment.arguments = bundle
    }
    supportFragmentManager.beginTransaction().replace(frameId, fragment).commit()
}