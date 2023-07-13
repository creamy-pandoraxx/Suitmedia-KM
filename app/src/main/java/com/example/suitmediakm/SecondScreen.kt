package com.example.suitmediakm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class SecondScreen : AppCompatActivity() {
    private lateinit var nameFromFirstScreenText: TextView
    private lateinit var selectedUserNameText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        nameFromFirstScreenText = findViewById(R.id.tv_name)
        selectedUserNameText = findViewById(R.id.selected_user)
        val chooseUserButton: Button = findViewById(R.id.btn_user)
        val backButton: ImageButton = findViewById(R.id.btn_back)

        val nameFromFirstScreen = intent.getStringExtra("nameFromFirstScreen")
        nameFromFirstScreenText.text = nameFromFirstScreen

        chooseUserButton.setOnClickListener {
            val intent = Intent(this@SecondScreen, ThirdScreen::class.java)
            startActivityForResult(intent, REQUEST_SELECT_USER)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_USER && resultCode == RESULT_OK) {
            val selectedUserName = data?.getStringExtra("selectedUserName")
            selectedUserName?.let {
                selectedUserNameText.text = it
            }
        }
    }

    companion object {
        private const val REQUEST_SELECT_USER = 1
    }
}