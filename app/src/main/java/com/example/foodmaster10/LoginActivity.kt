package com.example.foodmaster10

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmaster10.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val TAG = "SignInActivity"
    var mAuth: FirebaseAuth? = null
    var emailTextInput: EditText? = null
    var passwordTextInput: EditText? = null
    var signInButton: Button? = null
    var forgotPasswordButton: Button? = null
    var sendVerifyMailAgainButton: Button? = null
    var errorView: TextView? = null

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val emailTextInput = findViewById<EditText>(R.id.signInEmailTextInput)
        val passwordTextInput = findViewById<EditText>(R.id.signInPasswordTextInput)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)
        val sendVerifyMailAgainButton = findViewById<Button>(R.id.verifyEmailAgainButton)
        val errorView = findViewById<TextView>(R.id.signInErrorView)

        sendVerifyMailAgainButton.setVisibility(View.INVISIBLE)

        mAuth = FirebaseAuth.getInstance()

        checkUser()
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        signInButton.setOnClickListener(View.OnClickListener {
            if (emailTextInput.getText().toString().contentEquals("")) {
                errorView.setText("Email cant be empty")
            } else if (passwordTextInput.getText().toString().contentEquals("")) {
                errorView.setText("Password cant be empty")
            } else {
                mAuth!!.signInWithEmailAndPassword(emailTextInput.getText().toString(),
                    passwordTextInput.getText().toString())
                    .addOnCompleteListener(this,
                        OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = mAuth!!.currentUser
                                if (user != null) {
                                    if (user.isEmailVerified) {
                                        println("Email Verified : " + user.isEmailVerified)
                                        val HomeActivity = Intent(this,
                                            MainActivity::class.java)
                                        setResult(RESULT_OK, null)
                                        startActivity(HomeActivity)
                                        this.finish()
                                    } else {
                                        sendVerifyMailAgainButton.setVisibility(View.VISIBLE)
                                        errorView.setText("Please Verify your EmailID and SignIn")
                                    }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                                if (task.exception != null) {
                                    errorView.setText(task.exception!!.message)
                                }
                            }
                        })
            }
        })


        forgotPasswordButton.setOnClickListener{
            val forgotPasswordActivity =
                Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotPasswordActivity)
            this.finish()
        }


//        println("First commit")
    }
    private fun checkUser() {
        //if user already logged in profile activity
        //get current user
        if (mAuth?.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}