package com.ivan.letstalk.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.letstalk.R
import com.ivan.letstalk.model.CommonModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class MyHealthCardAdapter(var healthDetails: List<CommonModel>) :
    RecyclerView.Adapter<MyHealthCardAdapter.MovieVH>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_health_card_row, parent, false)
        return MovieVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val healthCard = healthDetails[position]
        // holder.tvName.text = healthCard.name
        holder.tvName.text = capitalizedString(healthCard.name)
        // holder.tvValueDetails.text = healthCard.valueDetails
        holder.tvValueDetails.text = capitalizedString(healthCard.valueDetails)
        if (healthCard.valueDetails == "1") {
            holder.tvValueDetails.text = context.resources.getString(R.string.male)
        } else if (healthCard.valueDetails == "2") {
            holder.tvValueDetails.text = context.resources.getString(R.string.female)
        }
        if (healthCard.valueDetails.isEmpty()) {
            holder.tvValueDetails.text = "N/A"
        }
        if (healthCard.name == "Age") {
            Log.d("healthCard", healthCard.valueDetails)
            val formatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val today: LocalDate = LocalDate.now()
            val birthday: LocalDate = LocalDate.parse(healthCard.valueDetails, formatter)


            val p: Period = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Period.between(birthday, today)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            println(
                "You are " + p.years.toString() + " years, " + p.months.toString() +
                        " months and " + p.days.toString() +
                        " days old."
            )
            holder.tvValueDetails.text = p.years.toString() + " years " + p.months.toString() +
                    " months " + p.days.toString() + " days"
        }
        /*val formatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("d/M/yyyy")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val today: LocalDate = LocalDate.now()
        val birthday: LocalDate = LocalDate.parse("1/1/1960", formatter)


        val p: Period = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Period.between(birthday, today)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        println(
            "You are " + p.years.toString() + " years, " + p.months.toString() +
                    " months and " + p.days.toString() +
                    " days old."
        )*/
    }

    private fun capitalizedString(name: String): String {
        var capitalizedString = ""
        if (name.trim { it <= ' ' } != "") {
            capitalizedString =
                name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }
        return capitalizedString
    }

    override fun getItemCount(): Int {
        return healthDetails.size
    }

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvValueDetails: TextView

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvValueDetails = itemView.findViewById(R.id.tv_value_details)
        }
    }

    companion object {
        private const val TAG = "MyHealthCardAdapter"
    }
}