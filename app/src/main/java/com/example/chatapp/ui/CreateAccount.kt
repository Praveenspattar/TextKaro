package com.example.chatapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.data.Account
import com.google.firebase.auth.FirebaseAuth
import com.praveen.chatapp.R

class CreateAccount : AppCompatActivity() {

    lateinit var account: Account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        account = Account(this)

        val user = account.getCurrentUser()

        if (user == null) {
            account.createAccount("praveen1@gmail.com","1#forgot")
        }else{
            Toast.makeText(this,"Logged in as ${user.email}",Toast.LENGTH_SHORT).show()
        }

    }
}