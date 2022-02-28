package pt.isec.theforgotten.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.theforgotten.firebase.databinding.ActivityMainBinding

// SHA1: A2:E6:C0:14:F6:FB:B5:07:DB:BC:55:88:6B:2D:8A:66:3F:85:61:18
// Web Client ID: 703733560851-ed2u5qdetmdkg336uinn2v79rfio86sf.apps.googleusercontent.com

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        auth = Firebase.auth

        // default_web_client_id always shows error, ignore
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        intent.extras?.apply {
            for (k in keySet()) {
                Log.i(TAG, "Extras: $k -> ${get(k)}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showUser(auth.currentUser)
    }
    fun showUser(user : FirebaseUser?) {
        val str = when (user) {
            null -> "No authenticated user"
            else -> "User: ${user.email}"
        }

        viewBinding.tvStatus.text = str
        Log.i(TAG, str)
    }

    fun onObservarDados(view: View) {
        val db = Firebase.firestore

        val rl = db.collection("Scores").document("Level1")
            .addSnapshotListener { docSS, e ->
                if (e!=null) {
                    return@addSnapshotListener
                }
                if (docSS!=null && docSS.exists()) {
                    val nrgames = docSS.getLong("nrgames")
                    val topscore = docSS.getLong("topscore")
                    Log.i(TAG, "$nrgames : $topscore")
                }
            }

        rl.remove()
    }

    fun onActualizarDados(view: View) {
        val db = Firebase.firestore
        val v = db.collection("Scores").document("Level1")

        /*
        v.get(Source.SERVER).addOnSuccessListener {
            v.update("nrgames", it.getLong("nrgames")!! + 1)
        }
        */

        db.runTransaction {transaction ->
            val doc = transaction.get(v)
            val newnrgames = doc.getLong("nrgames")!! + 1
            val newtopscore = doc.getLong("topscore")!! + 100
            transaction.update(v,"nrgames",newnrgames)
            transaction.update(v,"topscore",newtopscore)
            null
        }
    }

    fun onCriarDados(view: View) {
        val db = Firebase.firestore
        val scores = hashMapOf(
            "nrgames" to 0,
            "topscore" to 0
        )
        db.collection("Scores").document("Level1").set(scores)
    }

    fun onLogoutGmail(view: View) {
        googleSignInClient.signOut()
            .addOnCompleteListener(this) {
                showUser(null)
            }
    }

    fun onAutenticarGmail(view: View) {
        signInWithGoogle.launch(googleSignInClient.signInIntent)
    }

    fun onLogoutEmail(view: View) {
        signOut()
    }

    fun onAutenticarEmail(view: View) {
        signInWithEmail(
            viewBinding.edEmail.text.toString(),
            viewBinding.edPassword.text.toString()
        )
    }

    fun onRegistarEmail(view: View) {
        createUserWithEmail(
            viewBinding.edEmail.text.toString(),
            viewBinding.edPassword.text.toString()
        )
    }

    private fun createUserWithEmail(email:String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { _ ->
                Log.i(TAG, "createUser: success")
                showUser(auth.currentUser)
            }
            .addOnFailureListener(this) { e ->
                Log.i(TAG, "createUser: failure ${e.message}")
                showUser(null)
            }
    }

    private fun signInWithEmail(email:String,password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(this) { result ->
                Log.d(TAG, "signInWithEmail: success")
                showUser(auth.currentUser)
            }
            .addOnFailureListener(this) { e->
                Log.d(TAG, "signInWithEmail: failure ${e.message}")
                showUser(null)
            }
    }

    private val signInWithGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.i(TAG, "onActivityResult - Google authentication: failure")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener(this) { result ->
                Log.d(TAG, "signInWithCredential:success")
                showUser(auth.currentUser)
            }
            .addOnFailureListener(this) { e ->
                Log.d(TAG, "signInWithCredential:failure ${e.message}")
                showUser(auth.currentUser)
            }
    }

    private fun signOut() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
        showUser(auth.currentUser)
    }
}