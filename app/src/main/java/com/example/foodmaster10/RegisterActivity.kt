package com.example.foodmaster10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmaster10.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    private val TAG = "SignUpActivity"
    var mAuth: FirebaseAuth? = null
    var signUpButton: Button? = null
    var signUpEmailTextInput: EditText? = null
    var signUpPasswordTextInput: EditText? = null
    var agreementCheckBox: CheckBox? = null
    var errorView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mAuth = FirebaseAuth.getInstance()


        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        val signUpEmailTextInput = findViewById<EditText>(R.id.signUpEmailTextInput)
        val signUpPasswordTextInput = findViewById<EditText>(R.id.signUpPasswordTextInput)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val agreementCheckBox = findViewById<CheckBox>(R.id.agreementCheckbox)
        val errorView = findViewById<TextView>(R.id.signUpErrorView)


        signUpButton.setOnClickListener {
            if (signUpEmailTextInput.text.toString().contentEquals("")) {
                errorView.text = "Email cannot be empty"
            } else if (signUpPasswordTextInput.text.toString().contentEquals("")) {
                errorView.text = "Password cannot be empty"
            } else if (!agreementCheckBox.isChecked) {
                errorView.text = "Please agree to terms and Condition"
            } else {
                mAuth!!.createUserWithEmailAndPassword(signUpEmailTextInput.text.toString(),
                    signUpPasswordTextInput.text.toString())
                    .addOnCompleteListener(this,
                        OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = mAuth!!.currentUser
                                try {
                                    user?.sendEmailVerification()
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val alertDialogBuilder =
                                                    AlertDialog.Builder(
                                                        this)

                                                // set title
                                                alertDialogBuilder.setTitle("Please Verify Your EmailID")

                                                // set dialog message
                                                alertDialogBuilder
                                                    .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Sign In"
                                                    ) { dialog, id ->
                                                        val signInIntent =
                                                            Intent(this,
                                                                LoginActivity::class.java)
                                                        this.finish()
                                                    }

                                                // create alert dialog
                                                val alertDialog = alertDialogBuilder.create()

                                                // show it
                                                alertDialog.show()
                                            }
                                        }
                                } catch (e: Exception) {
                                    errorView.text = e.message
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                                if (task.exception != null) {
                                    errorView.text = task.exception!!.message
                                }
                            }
                        })
            }
        }


    }

}