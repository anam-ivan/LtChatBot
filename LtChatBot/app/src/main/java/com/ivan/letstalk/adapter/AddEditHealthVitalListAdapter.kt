package com.ivan.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivan.letstalk.R
import com.ivan.letstalk.model.healthVitals.HealthVitalsElement
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.healthVitals.MetaUpdateBodies
import com.ivan.letstalk.ui.AddHealthVitals


class AddEditHealthVitalListAdapter(var healthVitasElement: ArrayList<HealthVitalsElement>) :
    RecyclerView.Adapter<AddEditHealthVitalListAdapter.RegularizeDataVH>() {
    private lateinit var context: Context
    private val id: MutableList<String> = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegularizeDataVH {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.health_vitals_element_list_row, parent, false)
        return RegularizeDataVH(view)
    }

    override fun onBindViewHolder(holder: RegularizeDataVH, position: Int) {
        val healthCard = healthVitasElement[position]
        holder.tvTitle.text = healthCard.title
        holder.tvUnit.text = healthCard.unitValue
        holder.tvUnitValue.text = healthCard.unit
        Log.d("unitValue",healthCard.unit)
        // var num : Int = healthCard.unit.toInt()
        var num : Double = healthCard.unit.toDouble()
        try {
         num = healthCard.unit.toDouble()
        } catch (ex: NumberFormatException) {
            ex.printStackTrace()
        }
        holder.ivDecrease.setOnClickListener {
            num -= 0.5
            holder.tvUnitValue.text = num.toString()
        }
        holder.ivIncrease.setOnClickListener {
            num += 0.5
            holder.tvUnitValue.text = num.toString()
        }
        Glide.with(context).load("http://letstalk.dev13.ivantechnology.in/".plus(healthCard.icon))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ivHealthVitals)
        holder.btnUpdate.setOnClickListener {
            val body = MetaUpdateBodies.MetaUpdateBody(
                _id = healthCard._id,
                title = healthCard.title,
                // value = healthCard.unitValue,
                value = num.toString(),
                meta_id = healthCard.meta_id,
                date = healthCard.date,
                time = healthCard.time
            )
            (context as AddHealthVitals).updateHealthVitalsObservers(
                body
            )
        }
        holder.btnDelete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            builder.setTitle("Delete Vital")
            builder.setMessage("Are you sure you want to Delete?")
            // val customFont = Typeface.createFromAsset(assets, "font/gilroy_medium.ttf")
            builder.setPositiveButton("Yes"){ dialog,which->
                val pos: Int = holder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    /*Toast.makeText(
                        context,
                        "You clicked " + healthCard._id,
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
                id.add(healthCard._id)
                val body = MetaDeleteBodies.MetaDeleteBody(
                    // _id = healthCard._id,
                    _id = id,
                    status = "INACTIVE",
                )
                (context as AddHealthVitals).deleteHealthVitalsObservers(
                    body
                )
                deleteItem(pos)
            }
            builder.setNegativeButton("No"){dialog,which->
                dialog.dismiss()
            }
            /*builder.setNeutralButton("Cancel"){dialog,which->

            }*/
            builder.setOnCancelListener {

            }
            builder.setOnDismissListener {

            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.alert_dialog_back)
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            /*val pos: Int = holder.absoluteAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                Toast.makeText(
                    context,
                    "You clicked " + healthCard._id,
                    Toast.LENGTH_SHORT
                ).show()
            }
            val body = MetaDeleteBodies.MetaDeleteBody(
                _id = healthCard._id,
                status = "INACTIVE",
            )
            (context as AddHealthVitals).deleteHealthVitalsObservers(
                body
            )
            deleteItem(pos)*/
        }
    }

    /*fun removeItemAtPosition(position: Int) {
        healthVitasElement.removeAt(position)
    }*/

    private fun deleteItem(position: Int) {
        healthVitasElement.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, healthVitasElement.size)
        // holder.itemView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return healthVitasElement.size
    }

    inner class RegularizeDataVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvUnit: TextView
        var tvUnitValue: TextView
        var ivIncrease: ImageView
        var ivDecrease: ImageView
        var ivHealthVitals: ImageView
        var btnUpdate: MaterialButton
        var btnDelete: MaterialButton

        init {
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvUnit = itemView.findViewById(R.id.tv_unit)
            tvUnitValue = itemView.findViewById(R.id.tv_unit_value)
            ivIncrease = itemView.findViewById(R.id.iv_increase)
            ivDecrease = itemView.findViewById(R.id.iv_decrease)
            ivHealthVitals = itemView.findViewById(R.id.iv_health_vitals)
            btnUpdate = itemView.findViewById(R.id.btn_update)
            btnDelete = itemView.findViewById(R.id.btn_delete)
        }
    }

    companion object {
        private const val TAG = "AddEditHealthVitalListAdapter"
    }
}