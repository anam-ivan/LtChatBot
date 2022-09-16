package com.ivan.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivan.letstalk.R
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.myHealthVitals.Value
import com.ivan.letstalk.ui.AddHealthVitals
import com.ivan.letstalk.ui.MyHealthVitals
import java.io.Serializable

class HealthVitalsElementsAdapter(private var healthElementDetails: ArrayList <Value>) :
    RecyclerView.Adapter<HealthVitalsElementsAdapter.HealthVitalsVH>() {
    private lateinit var context: Context
    private val id: MutableList<String> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthVitalsVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.child_data_list, parent, false)
        return HealthVitalsVH(view)
    }

    override fun onBindViewHolder(holder: HealthVitalsVH, position: Int) {
        val healthCard = healthElementDetails[position]
        // healthCard.vital[position].Id?.let { Log.d("id", it) }
        holder.tvTime.text = healthElementDetails[position].time.toString()
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, AddHealthVitals::class.java)
            intent.putExtra("isFromHealthVitalCard",true)
            intent.putExtra("position",position)
            // intent.putExtra("data", healthElementDetails as Serializable)
            intent.putExtra("data", healthCard as Serializable)
            context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            builder.setTitle("Delete Vital")
            builder.setMessage("Are you sure you want to Delete?")
            builder.setPositiveButton("Yes"){ dialog,which->
                val pos: Int = holder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                }
                healthCard.vital[pos].Id?.let { it1 -> id.add(it1) }
                val body = MetaDeleteBodies.MetaDeleteBody(
                    // _id = healthCard._id,
                    _id = id,
                    status = "INACTIVE",
                )
                /*val body = healthCard.vital[pos].Id?.let { _ ->
                    MetaDeleteBodies.MetaDeleteBody(
                        // _id = healthCard._id,
                        _id = id,
                        status = "INACTIVE",
                    )
                }*/
                (context as MyHealthVitals).deleteHealthVitalsObservers(
                    body
                )
                deleteItem(pos)
                // holder.itemView.visibility = View.GONE
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.setOnCancelListener {

            }
            builder.setOnDismissListener {

            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.alert_dialog_back)
            dialog.show()
            val window: Window = dialog.window!!
            window.setLayout(800, 500)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
        val exampleAdapter = ExampleAdapter(healthCard.vital)
        holder.childRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        holder.childRecyclerView.adapter = exampleAdapter
    }

    private fun deleteItem(position: Int) {
        healthElementDetails.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, healthElementDetails.size)
        // holder.itemView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        Log.d("health_vitals", healthElementDetails.size.toString())
        return healthElementDetails.size
    }

    inner class HealthVitalsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var childRecyclerView : RecyclerView
        var tvTime : AppCompatTextView
        var btnDelete : AppCompatTextView
        var btnEdit : AppCompatTextView

        init {
            childRecyclerView = itemView.findViewById(R.id.child_recycler_view)
            tvTime = itemView.findViewById(R.id.tv_time)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            btnEdit = itemView.findViewById(R.id.btn_edit)
        }
    }

    companion object {
        private const val TAG = "HealthVitalsElementsAdapter"
    }
}