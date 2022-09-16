package com.ivan.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivan.letstalk.R
import com.ivan.letstalk.model.chat.ChatPossibleProblems
import com.ivan.letstalk.model.deleteDocuments.MetaDeleteDocuments
import com.ivan.letstalk.model.documents.MData
import com.ivan.letstalk.ui.FileViewerActivity
import com.ivan.letstalk.ui.MyDocumentsActivity
import java.net.URLConnection
import java.text.SimpleDateFormat

class DocumentsListAdapter(var documentList: ArrayList<MData>,private val click: onClickListener) :
    RecyclerView.Adapter<DocumentsListAdapter.DocumentsListVH>() {
    private lateinit var context : Context
    private var havePermission: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsListVH {
        context  = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.documents_list_row, parent, false)
        return DocumentsListVH(view)
    }

    override fun onBindViewHolder(holder: DocumentsListVH, position: Int) {
        val faqs = documentList[position]
        holder.tvDocCategoryName.text = faqs.documentCategoryName
        // holder.tvDate.text = faqs.recordDate
        if (!faqs.recordDate.isNullOrEmpty()) {
            val date = faqs.recordDate
            var spf = SimpleDateFormat("yyyy-MM-dd")
            val newDate = spf.parse(date)
            spf = SimpleDateFormat("dd MMM yy")
            val newDateString = newDate?.let { spf.format(it) }
            println(newDateString)
            Log.d("newDateString", newDateString.toString())
            holder.tvDate.text = newDateString
        }
        holder.tvTime.text = faqs.recordTime
        holder.tvDocTitle.text = faqs.documentName
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FileViewerActivity::class.java)
            intent.putExtra("filePath", faqs.files[0])
            context.startActivity(intent)
        }
        val filePath = faqs.files[0]
        holder.ivDownload.setOnClickListener {
            click.onClick(position)
            if (havePermission) {
                if (filePath.isNotEmpty()) {
                    (context as MyDocumentsActivity).downloadFile("http://letstalk.dev13.ivantechnology.in/$filePath")
                }
            } else {

            }
            // (context as MyDocumentsActivity).checkPermission()
            /*if (filePath.isNotEmpty()) {
                (context as MyDocumentsActivity).downloadFile("http://letstalk.dev13.ivantechnology.in/$filePath")
            }*/
        }
        if (isImageFile("http://letstalk.dev13.ivantechnology.in/$filePath")) {
            holder.ivDocType.setBackgroundResource(R.drawable.ic_image)
        } else {
            holder.ivDocType.setBackgroundResource(R.drawable.ic_pdf)
        }
        holder.ivDelete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog)
            builder.setTitle("Delete Document")
            builder.setMessage("Are you sure you want to Delete?")
            builder.setPositiveButton("Yes"){ dialog,which->
                val pos: Int = holder.absoluteAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                }
                val body = faqs.Id?.let { it1 ->
                    MetaDeleteDocuments.DeleteDocuments(
                        _id = it1,
                        status = "INACTIVE",
                    )
                }
                if (body != null) {
                    (context as MyDocumentsActivity).deleteDocuments(
                        body
                    )
                }
                deleteItem(pos)
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
            window.setLayout(800, 450)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
    }

    private fun deleteItem(position: Int) {
        documentList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, documentList.size)
        // holder.itemView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    private fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }

    inner class DocumentsListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // var faqTitle: TextView
        var tvDocCategoryName: TextView
        var tvDate: TextView
        var tvTime: TextView
        var tvDocTitle: TextView
        var ivDownload: ImageView
        var ivDocType: ImageView
        var ivDelete: ImageView
        // var faqDesc: TextView

        init {
            /*faqTitle = itemView.findViewById(R.id.faq_title)
            faqDesc = itemView.findViewById(R.id.faq_desc)*/
            tvDocCategoryName = itemView.findViewById(R.id.tv_doc_category_name)
            tvDate = itemView.findViewById(R.id.tv_date)
            tvTime = itemView.findViewById(R.id.tv_time)
            tvDocTitle = itemView.findViewById(R.id.tv_doc_title)
            ivDownload = itemView.findViewById(R.id.iv_download)
            ivDelete = itemView.findViewById(R.id.iv_delete)
            ivDocType = itemView.findViewById(R.id.iv_doc_type)
        }
    }

    companion object {
        private const val TAG = "DocumentsListAdapter"
    }

    fun getResult(value: Boolean) {
        if (value) {
            Log.d("permission", value.toString())
            havePermission = true
        } else {
            Log.d("permission", value.toString())
            havePermission = false
        }
    }

    interface onClickListener {
        fun onClick(pos: Int)
    }
}