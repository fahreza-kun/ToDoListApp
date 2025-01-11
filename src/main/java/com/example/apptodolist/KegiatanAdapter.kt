package com.example.apptodolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class KegiatanAdapter(
    private val kegiatanList: MutableList<Kegiatan>,
    private val onComplete: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Int) -> Unit
) : RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kegiatan, parent, false)
        return KegiatanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KegiatanViewHolder, position: Int) {
        val kegiatan = kegiatanList[position]
        holder.bind(kegiatan)
    }

    override fun getItemCount(): Int = kegiatanList.size

    inner class KegiatanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaKegiatan: TextView = itemView.findViewById(R.id.tvNamaKegiatan)
        private val tvTargetSelesai: TextView = itemView.findViewById(R.id.tvTargetSelesai)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)

        fun bind(kegiatan: Kegiatan) {
            tvNamaKegiatan.text = kegiatan.namaKegiatan
            tvTargetSelesai.text = kegiatan.targetSelesai
            checkBox.isChecked = kegiatan.selesai

            updateTextAppearance(kegiatan.selesai)
            updateDueDate(kegiatan)

            checkBox.setOnClickListener {
                if (checkBox.isChecked && !kegiatan.selesai) {
                    onComplete(adapterPosition)
                } else if (!checkBox.isChecked && kegiatan.selesai) {
                    checkBox.isChecked = true // Prevent unchecking
                }
            }

            btnDelete.setOnClickListener {
                onDelete(adapterPosition)
            }

            btnEdit.setOnClickListener {
                onEdit(adapterPosition)
            }


            btnEdit.isEnabled = !kegiatan.selesai
            btnEdit.alpha = if (kegiatan.selesai) 0.5f else 1.0f
        }

        private fun updateTextAppearance(isCompleted: Boolean) {
            tvNamaKegiatan.paintFlags = if (isCompleted) {
                tvNamaKegiatan.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvNamaKegiatan.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        private fun updateDueDate(kegiatan: Kegiatan) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            if (kegiatan.targetSelesai < currentDate && !kegiatan.selesai) {
                tvTargetSelesai.text = "TERLEWAT (${kegiatan.targetSelesai})"
                tvTargetSelesai.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
            } else {
                tvTargetSelesai.text = kegiatan.targetSelesai
                tvTargetSelesai.setTextColor(itemView.context.getColor(android.R.color.black))
            }
        }
    }
}