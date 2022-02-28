package pt.isec.amov.ajp.reversi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.ajp.reversi.data.Constants
import pt.isec.amov.ajp.reversi.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity(){
    private lateinit var binding : ActivityScoreBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        val name = intent.getStringExtra(Constants.INTENT_PLAYER_NAME)
        val collection = db.collection("TopScores")

        var topScoresList = ArrayList<Int>()

        collection
            .orderBy("score", Query.Direction.DESCENDING)
            .whereEqualTo("player", name)
            .get()
            .addOnSuccessListener {
                for (i in 0 until it.size())
                    topScoresList.add(it.documents[i].getLong("score")!!.toInt())
            }



        if (topScoresList.size == 0) return

        if (topScoresList.size >= 1)
            binding.score1.text = topScoresList[0].toString()

        if (topScoresList.size >= 2)
            binding.score1.text = topScoresList[1].toString()

        if (topScoresList.size >= 3)
            binding.score1.text = topScoresList[2].toString()

        if (topScoresList.size >= 4)
            binding.score1.text = topScoresList[3].toString()

        if (topScoresList.size == 5)
            binding.score1.text = topScoresList[4].toString()

    }
}