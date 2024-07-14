package com.example.chatapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.utils.MyLifecycleObserver
import com.praveen.chatapp.R


class ProfileActivity : AppCompatActivity() {

    lateinit var editBtn : ImageView
    lateinit var ProfileImage : ImageView
    lateinit var nameTV : TextView
    lateinit var myEditText : EditText
    lateinit var observer : MyLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editBtn = findViewById(R.id.editimg)
        nameTV = findViewById(R.id.profileName)
        myEditText = findViewById(R.id.myEditText)
        ProfileImage = findViewById(R.id.ProfileImageV)

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        editBtn.setOnClickListener{
            myEditText.visibility = View.VISIBLE
            myEditText.setText(nameTV.getText().toString())
            myEditText.requestFocus()
            imm.showSoftInput(myEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        myEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                nameTV.text = myEditText.text.toString()
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                v.visibility = View.GONE

                return@OnEditorActionListener true
            }
            false
        })

        ProfileImage.setOnClickListener {
            observer.selectImage()
        }

        observer = MyLifecycleObserver(activityResultRegistry) {
            ProfileImage.setImageURI(it)
        }

        lifecycle.addObserver(observer)

    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (myEditText.visibility == View.VISIBLE){
            myEditText.visibility = View.GONE
        }else
            super.onBackPressed()
    }

}