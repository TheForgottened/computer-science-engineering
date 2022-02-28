package pt.isec.ans.rascunhos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pt.isec.ans.rascunhos.R
import pt.isec.ans.rascunhos.databinding.ActivityFundoImagemBinding
import pt.isec.ans.rascunhos.databinding.ActivityHistoryBinding
import pt.isec.ans.rascunhos.modelo.ListaRascunhos
import pt.isec.ans.rascunhos.modelo.Rascunho

class HistoryActivity : AppCompatActivity() {
    private lateinit var b : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.lvHistory.adapter = HistoryAdapter()
    }

    class HistoryAdapter : BaseAdapter() {
        override fun getCount(): Int = ListaRascunhos.lista.size

        override fun getItem(position: Int): Any = ListaRascunhos.lista[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater
                .from(parent!!.context)
                .inflate(R.layout.history_item, parent, false)

            val rascunho = ListaRascunhos.lista[position]
            view.findViewById<TextView>(R.id.t

            // TODO: falta aqui muito código kekw

            return view
        }

    }
}