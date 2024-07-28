package com.example.chatapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MyApp : Application() {
    companion object {
        lateinit var firestore: FirebaseFirestore
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        FirestoreUtil.initialize()
    }
}

object FirestoreUtil {
    private lateinit var firestore: FirebaseFirestore

    fun initialize() {
        firestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    fun getFirestoreInstance(): FirebaseFirestore {
        return firestore
    }
}