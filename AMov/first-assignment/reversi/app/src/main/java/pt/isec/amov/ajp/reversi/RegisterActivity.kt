package pt.isec.amov.ajp.reversi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = Firebase.auth
        db = Firebase.firestore
        setContentView(R.layout.activity_register)
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun reload(){

    }

    fun onClickRegister(view: View) {

        val username = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        if (password.text.toString().isEmpty() || email.text.toString().isEmpty()){
            updateUI(view, null)
        }

        mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Snackbar.make(view, "Account created successfully", Snackbar.LENGTH_SHORT).show()

                    val user = mAuth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        displayName = username.text.toString()
                        photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User profile updated.")
                            }
                        }

                    val date = Calendar.getInstance().time

                    val newUser = hashMapOf(
                        "username" to username.text.toString(),
                        "email" to email.text.toString(),
                        "date_created" to date
                    )

                    db.collection("User").document(user.uid)
                        .set(newUser)
                        .addOnSuccessListener { Log.d(tagCF, "New user document successfully written!") }
                        .addOnFailureListener { e -> Log.d(tagCF, "Error writing user document.") }
                    updateUI(view, user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Snackbar.make(view, "Authentication failed.",
                        Snackbar.LENGTH_SHORT).show()
                    updateUI(view, null)
                }
            }
    }

    private fun updateUI(view: View ,user: FirebaseUser?) {
        if(user != null) setContentView(R.layout.activity_main)
        else Snackbar.make(view, "Please check if you filled every field correctly.", Snackbar.LENGTH_SHORT).show()
    }


    fun onClickSignInInstead(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val tagCF = "CloudFirestore"
    }
}