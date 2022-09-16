package com.ivan.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.chat.ChatPossibleProblems

class ChatPossibleProblemsAdapter(var problemList: List<ChatPossibleProblems>, private val click:onClickListener) :
    RecyclerView.Adapter<ChatPossibleProblemsAdapter.MovieVH>() {
    private lateinit var context : Context
    private var chatPossibleProblemsPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        context  = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_possible_problems_row, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val possibleProblems = problemList[position]
        holder.tvPossibleProblems.text = possibleProblems.possibleProblems
        /*if (problemList[position].isSelect) {
            holder.tvPossibleProblems.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.rootView.setBackgroundResource(R.drawable.layout_red_back)
            holder.rootView.setPadding(20,20,20,20)
        } else {

        }*/
        holder.rootView.setOnClickListener {
            click.onClick(position)
            /*problemList[position].isSelect = true
            notifyDataSetChanged()*/
            /*chatPossibleProblemsPosition = holder.absoluteAdapterPosition
            holder.tvPossibleProblems.setTextColor(ContextCompat.getColor(context,R.color.white))
            holder.rootView.setBackgroundResource(R.drawable.layout_red_back)
            holder.rootView.setPadding(20,20,20,20)*/
        }
    }

    override fun getItemCount(): Int {
        return problemList.size
    }

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPossibleProblems: TextView
        var rootView: RelativeLayout

        init {
            tvPossibleProblems = itemView.findViewById(R.id.tv_possible_problem)
            rootView = itemView.findViewById(R.id.rootView)
        }
    }

    fun passPosition(mPosition: Int): Int {
        return mPosition
    }

    companion object {
        private const val TAG = "ChatPossibleProblemsAdapter"
    }
    interface onClickListener{
        fun onClick(pos:Int)
        fun onTextClick(data: ChatPossibleProblems?)
    }
}