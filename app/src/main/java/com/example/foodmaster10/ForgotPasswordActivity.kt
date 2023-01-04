package com.example.foodmaster10

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.example.foodmaster10.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding

    private val TAG = "ForgotPasswordActivity"
    var mAuth: FirebaseAuth? = null
    var resetPasswordButton: Button? = null
    var emailTextInput: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailTextInput = findViewById<EditText>(R.id.fpEmailTextInput)
        val resetPasswordButton = findViewById<Button>(R.id.resetPasswordButton)
        mAuth = FirebaseAuth.getInstance()


        resetPasswordButton.setOnClickListener(View.OnClickListener {
            mAuth!!.sendPasswordResetEmail(emailTextInput.getText().toString())
                .addOnCompleteListener { task ->

                    val alertDialogBuilder = AlertDialog.Builder(
                        this@ForgotPasswordActivity)

                    // set title
                    alertDialogBuilder.setTitle("Reset Password")

                    // set dialog message
                    alertDialogBuilder
                        .setMessage("A Reset Password Link Is Sent To Your Registered EmailID")
                        .setCancelable(false)
                        .setPositiveButton("OK"
                        ) { dialog, id -> finish() }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
        })


    }
}