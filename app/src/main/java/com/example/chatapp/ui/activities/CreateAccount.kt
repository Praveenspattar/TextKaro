package com.example.chatapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.utils.Account
import com.praveen.chatapp.R

class CreateAccount : AppCompatActivity() {

    lateinit var account: Account
    lateinit var creatBtn: Button
    lateinit var emailET: EditText
    lateinit var passwordET: EditText
    lateinit var signinArrow: ImageView
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
        creatBtn = findViewById(R.id.creatBtn)
        emailET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)
        signinArrow = findViewById(R.id.signinArrow)

        val user = account.getCurrentUser()

        Thread.sleep(1000)
        installSplashScreen()
        if (user != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        signinArrow.setOnClickListener {
            val intent = Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }

        creatBtn.setOnClickListener{
            if (isValidEmail(emailET.text.toString())) {

                if (emailET.text.toString().isNotEmpty() && passwordET.text.toString().isNotEmpty()) {
                    account.createAccount(emailET.text.toString(),passwordET.text.toString())
                } else {
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@gmail.com$".toRegex()
        return email.matches(emailRegex)
    }
}