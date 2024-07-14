package com.example.chatapp.utils

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MyLifecycleObserver(
    private val registry: ActivityResultRegistry,
    private val onImageSelected: (Uri) -> Unit
) : DefaultLifecycleObserver {
    lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("key", owner, ActivityResultContracts.GetContent()) {
            if (it != null) onImageSelected(it)
        }
    }

    fun selectImage() {
        getContent.launch("image/*")
    }
}