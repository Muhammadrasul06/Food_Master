package com.example.foodmaster10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.foodmaster10.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var actionBar: ActionBar
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.setTitle("Profile")

        mAuth = FirebaseAuth.getInstance()
        checkUser()


        binding.btnLogOut.setOnClickListener {
            mAuth.signOut()
            checkUser()
        }
        println("Third commit")
    }
    private fun checkUser() {
        val firebaseUser = mAuth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email
            binding.txtEmail.text = email
        }
        else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}