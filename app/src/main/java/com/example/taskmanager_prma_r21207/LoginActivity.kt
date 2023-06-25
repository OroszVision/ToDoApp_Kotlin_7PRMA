package com.example.taskmanager_prma_r21207

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth


private lateinit var emailEditText: EditText
private lateinit var passwordEditText: EditText
private lateinit var loginButton: Button
private lateinit var progressBar: ProgressBar
private lateinit var signupTextViewButton: TextView


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)
        signupTextViewButton = findViewById(R.id.signupTextViewButton)

        loginButton.setOnClickListener{ loginUser()}
        signupTextViewButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()


        val isValidated = validateData(email,password)

        if(!isValidated){
            return
        }

        loginAccountInFirebase(email,password)
    }

    private fun loginAccountInFirebase(email: String, password: String) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    if(firebaseAuth.currentUser?.isEmailVerified == true){
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Utility.showToast(this, "Email not verified! Please verify your email")
                    }
                } else {
                    // Account creation failed
                    val exception = task.exception
                    // Handle the exception or display an error message to the user
                    Toast.makeText(this, "Account creation failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeInProgress(inProgress : Boolean){
        if(inProgress) {
            progressBar.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        }
    }

    private fun validateData(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Email is invalid"
            return false
        }

        if (password.length < 8) {
            passwordEditText.error = "Password should be at least 8 characters long"
            return false
        }
        return true
    }
}