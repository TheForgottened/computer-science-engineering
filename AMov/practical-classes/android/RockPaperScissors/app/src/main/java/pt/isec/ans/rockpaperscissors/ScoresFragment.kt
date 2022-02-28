package pt.isec.ans.rockpaperscissors

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class ScoresFragment : Fragment() {
    lateinit var model : GameViewModel
    lateinit var tvMyWins     : TextView
    lateinit var tvOtherWins  : TextView
    lateinit var tvTotalGames : TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scores, container, false)
        tvMyWins     = view.findViewById(R.id.tvMyWins)
        tvOtherWins  = view.findViewById(R.id.tvOtherWins)
        tvTotalGames = view.findViewById(R.id.tvTotalGames)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = ViewModelProvider(activity!!).get(GameViewModel::class.java)
        model.state.observe(activity!!) {
            tvMyWins.text = model.myWins.toString()
            tvMyWins.setBackgroundColor(if (model.lastVictory== ME) Color.argb(128,0,255,0) else Color.WHITE)
            tvOtherWins.text = model.otherWins.toString()
            tvOtherWins.setBackgroundColor(if (model.lastVictory== OTHER) Color.argb(128,255,0,0) else Color.WHITE)
            tvTotalGames.text = model.totalGames.toString()

        }
    }
}