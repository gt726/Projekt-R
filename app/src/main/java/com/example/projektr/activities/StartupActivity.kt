package com.example.projektr.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projektr.R
import com.google.firebase.auth.FirebaseAuth

class StartupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_startup)

        // inicijaliziraj FirebaseAuth
        auth = FirebaseAuth.getInstance()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // postavi onClickListener za gumb "Log In"
        findViewById<Button>(R.id.log_in_btn).setOnClickListener {
            // otvori LoginActivity kada se pritisne gumb "Log In"
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // postavi onClickListener za gumb "Sign Up"
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener {
            // otvori RegisterActivity kada se pritisne gumb "Sign Up"
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()

        // Provjeri je li korisnik već prijavljen
        val currentUser = auth.currentUser
        // Ako je korisnik prijavljen, preusmjeri ga na MainActivity
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optionally close the registration activity
        }
    }
}