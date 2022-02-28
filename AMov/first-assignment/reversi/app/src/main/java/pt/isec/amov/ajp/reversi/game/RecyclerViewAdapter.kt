package pt.isec.amov.ajp.reversi.game

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import pt.isec.amov.ajp.reversi.R

class RecyclerViewAdapter(private val mContext: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private val gameViewModel = GameViewModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext)
        val view = mInflater.inflate(R.layout.card_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            gameViewModel.makeMove(position)
            notifyDataSetChanged()
        }

        holder.gridSquare.height

        when (gameViewModel.gameModel.board[position]) {
            -1 -> holder.gridSquare.visibility = View.INVISIBLE
            0 -> {
                holder.gridSquare.visibility = View.VISIBLE
                holder.gridSquare.setCardForegroundColor(mContext.getColorStateList(R.color.dracula_cyan))
            }
            1 -> {
                holder.gridSquare.visibility = View.VISIBLE
                holder.gridSquare.setCardForegroundColor(mContext.getColorStateList(R.color.dracula_red))
            }
            2 -> {
                holder.gridSquare.visibility = View.VISIBLE
                holder.gridSquare.setCardForegroundColor(mContext.getColorStateList(R.color.dracula_purple))
            }
        }

        if (gameViewModel.getAllPossibleMoves(gameViewModel.gameModel.currentPlayer.value!!).contains(position)) {
            holder.gridSquare.visibility = View.VISIBLE

            val color = ColorUtils.setAlphaComponent(
                mContext.getColor(R.color.dracula_background),
                100
            )

            holder.gridSquare.setCardForegroundColor(ColorStateList.valueOf(color))
        }
    }

    override fun getItemCount(): Int {
        return gameViewModel.gameModel.board.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gridSquare : MaterialCardView = itemView.findViewById(R.id.card_view_square)
    }
}