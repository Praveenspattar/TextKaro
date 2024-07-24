package com.example.chatapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.utils.Account
import com.praveen.chatapp.R

class SigninActivity : AppCompatActivity() {

    lateinit var account: Account
    lateinit var creatBtn: Button
    lateinit var emailET: EditText
    lateinit var passwordET: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        account = Account(this)
        creatBtn = findViewById(R.id.creatBtn)
        emailET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)

        creatBtn.setOnClickListener{
            if (emailET.text.toString().isNotEmpty() && passwordET.text.toString().isNotEmpty()) {
                account.signIn(emailET.text.toString(),passwordET.text.toString())
            } else {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}