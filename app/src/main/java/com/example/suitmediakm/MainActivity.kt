package com.example.suitmediakm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var sentenceInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        nameInput = findViewById(R.id.et_name)
        sentenceInput = findViewById(R.id.et_palindrome)
        val checkButton: Button = findViewById(R.id.btn_check)
        val nextButton: Button = findViewById(R.id.btn_next)


        checkButton.setOnClickListener {
            checkPalindrome()
        }

        nextButton.setOnClickListener {
            val name = nameInput.text.toString()
            val intent = Intent(this, SecondScreen::class.java)
            intent.putExtra("nameFromFirstScreen", name)
            startActivity(intent)
        }
    }
    private fun checkPalindrome() {
        val sentence = sentenceInput.text.toString().toLowerCase().replace("\\s+".toRegex(), "")
        var isPalindrome = false

        if (!TextUtils.isEmpty(sentence)) {
            val reversedSentence = StringBuilder(sentence).reverse().toString()
            isPalindrome = sentence == reversedSentence
        }

        val message = if (isPalindrome) "isPalindrome" else "not palindrome"
        showMessageDialog(message)
    }

    private fun showMessageDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}