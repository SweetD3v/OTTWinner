package com.example.ottwinner.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ottwinner.R
import com.example.ottwinner.databinding.ActivityLoginBinding
import com.example.ottwinner.utils.toastCustomView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResult(task)
                } catch (e: Exception) {
                    Log.e("TAG", "loginExc: ${e.message}")
                }
            }
        }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            Log.e("TAG", "handleResult: ${account}")
            if (account != null) {
                updateUI(account)
            }
        } else {
            Log.e("TAG", "handleResultExc: ${task.exception}")
            toastCustomView(task.exception.toString(), Toast.LENGTH_SHORT, "#ff0000")
//            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        Log.e("TAG", "updateUI: ${account.idToken}")
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credentials)?.addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("email", account.email)
                    putExtra("name", account.displayName)
                }
                startActivity(intent)
            } else {
                toastCustomView(it.exception.toString(), Toast.LENGTH_SHORT, "#ff0000")
//                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

        binding.run {
            btnLogin.setOnClickListener {
                startSignIn()
            }
        }
    }

    private fun startSignIn() {
        val signInIntent = googleSignInClient?.signInIntent
        Log.e("TAG", "startSignIn: ${signInIntent}")
        signInLauncher.launch(signInIntent)
    }
}