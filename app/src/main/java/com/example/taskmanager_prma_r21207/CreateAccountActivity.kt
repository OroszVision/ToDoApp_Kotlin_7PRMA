package com.example.taskmanager_prma_r21207

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginTextViewButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        createAccountButton = findViewById(R.id.createAccountButton)
        progressBar = findViewById(R.id.progressBar)
        loginTextViewButton = findViewById(R.id.loginTextViewButton)


        createAccountButton.setOnClickListener { createAccount() }
        loginTextViewButton.setOnClickListener { finish()}

    }
    private fun createAccount(){
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        val isValidated = validateData(email,password,confirmPassword)

        if(!isValidated){
            return
        }

        createAccountInFirebase(email,password)

    }

    private fun createAccountInFirebase(email : String, password: String){
        changeInProgress(true)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    Utility.showToast(this, "Account created successfully")
                    firebaseAuth.currentUser?.sendEmailVerification()
                    firebaseAuth.signOut()
                    finish()
                } else {
                    // Account creation failed
                    val exception = task.exception
                    // Handle the exception or display an error message to the user
                    Utility.showToast(this, "Account creation failed: ${exception?.message}")
                }
            }
    }

    private fun changeInProgress(inProgress : Boolean){
        if(inProgress) {
            progressBar.visibility = View.VISIBLE
            createAccountButton.visibility = View.GONE
        }else{
            progressBar.visibility = View.GONE
            createAccountButton.visibility = View.VISIBLE
        }
    }

    private fun validateData(email: String, password: String, confirmPass: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Email is invalid"
            return false
        }

        if (password.length < 8) {
            passwordEditText.error = "Password should be at least 8 characters long"
            return false
        }

        if (password != confirmPass) {
            confirmPasswordEditText.error = "Passwords do not match"
            return false
        }
        return true
    }
}