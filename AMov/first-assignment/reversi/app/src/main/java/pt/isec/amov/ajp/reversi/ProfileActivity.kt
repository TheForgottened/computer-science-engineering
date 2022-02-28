package pt.isec.amov.ajp.reversi

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.ajp.reversi.data.Constants
import pt.isec.amov.ajp.reversi.database.ReversiLocalReader
import pt.isec.amov.ajp.reversi.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.name.text = ReversiLocalReader.getName()

        db = Firebase.firestore

        if (ReversiLocalReader.hasAvatar()) {
            val bitmap = BitmapFactory.decodeFile(ReversiLocalReader.getAvatarPath())
            binding.imageViewAvatar.setImageBitmap(bitmap)
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.name.text = ReversiLocalReader.getName()

        if (ReversiLocalReader.hasAvatar()) {
            val bitmap = BitmapFactory.decodeFile(ReversiLocalReader.getAvatarPath())
            binding.imageViewAvatar.setImageBitmap(bitmap)
        }
    }

    fun onClickNewAvatar(view: android.view.View) {
        val intent = Intent(this, ImageCaptureActivity::class.java)
        startActivity(intent)
    }

    fun onClickChangeName(view: android.view.View) {
        val textBox = EditText(this).apply {
            maxLines = 1
            filters = arrayOf(object : InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                    if (source?.none { it.isLetterOrDigit()} == true)
                        return ""
                    return null
                }
            })
        }

        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.change_name))
            .setMessage(resources.getString(R.string.change_name_description))
            .setView(textBox)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                val newName = textBox.text

                if (newName.isBlank()) {
                    Toast.makeText(this, R.string.change_name_error, Toast.LENGTH_LONG).show()
                } else {
                    ReversiLocalReader.setName(newName.toString())
                    onRestart()
                }
            }
            .show()
    }

    fun onClickViewScores(view: View) {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra(Constants.INTENT_PLAYER_NAME, binding.name.text)
        startActivity(intent)
    }
}