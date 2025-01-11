package com.example.apptodolist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnPekerjaan: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var kegiatanAdapter: KegiatanAdapter
    private val kegiatanList = mutableListOf<Kegiatan>()
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPekerjaan = findViewById(R.id.btnPekerjaan)
        recyclerView = findViewById(R.id.recyclerView)

        kegiatanAdapter = KegiatanAdapter(
            kegiatanList,
            { position -> markAsCompleted(position) },
            { position -> deleteKegiatan(position) },
            { position -> showUpdateDialog(position) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = kegiatanAdapter

        btnPekerjaan.setOnClickListener {
            showUploadDialog()
        }
    }

    private fun showUploadDialog() {
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.upload_list, null)
        dialog.setContentView(dialogView)

        val editTextJobName = dialogView.findViewById<EditText>(R.id.editTextJobName)
        val calendarView = dialogView.findViewById<CalendarView>(R.id.calendarView)
        val btnSimpan = dialogView.findViewById<Button>(R.id.btnSimpan)

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        }

        btnSimpan.setOnClickListener {
            val jobName = editTextJobName.text.toString()

            if (jobName.isNotEmpty() && selectedDate.isNotEmpty()) {
                val kegiatan = Kegiatan(jobName, selectedDate, false)
                kegiatanList.add(kegiatan)
                sortList()
                kegiatanAdapter.notifyDataSetChanged()

                Toast.makeText(this, "Kegiatan $jobName berhasil disimpan", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                editTextJobName.text.clear()
            } else {
                Toast.makeText(this, "Harap isi nama kegiatan dan pilih tanggal", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun showUpdateDialog(position: Int) {
        if (position < 0 || position >= kegiatanList.size) return

        val kegiatan = kegiatanList[position]
        val dialog = Dialog(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.upload_list, null)
        dialog.setContentView(dialogView)

        val editTextJobName = dialogView.findViewById<EditText>(R.id.editTextJobName)
        val calendarView = dialogView.findViewById<CalendarView>(R.id.calendarView)
        val btnSimpan = dialogView.findViewById<Button>(R.id.btnSimpan)

        // Set nilai awal
        editTextJobName.setText(kegiatan.namaKegiatan)
        selectedDate = kegiatan.targetSelesai

        // Set tanggal di CalendarView
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(kegiatan.targetSelesai)
            date?.let {
                calendarView.date = it.time
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        }

        btnSimpan.setOnClickListener {
            val jobName = editTextJobName.text.toString()

            if (jobName.isNotEmpty() && selectedDate.isNotEmpty()) {
                // Update kegiatan yang ada
                kegiatanList[position] = Kegiatan(jobName, selectedDate, kegiatan.selesai)
                sortList()
                kegiatanAdapter.notifyDataSetChanged()

                Toast.makeText(this, "Kegiatan berhasil diupdate", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Harap isi nama kegiatan dan pilih tanggal", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun markAsCompleted(position: Int) {
        if (position < 0 || position >= kegiatanList.size) return

        val kegiatan = kegiatanList[position]
        kegiatan.selesai = true
        sortList()
        kegiatanAdapter.notifyDataSetChanged()

        Toast.makeText(
            this,
            "Kegiatan ${kegiatan.namaKegiatan} telah selesai",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteKegiatan(position: Int) {
        if (position < 0 || position >= kegiatanList.size) return

        val kegiatan = kegiatanList[position]
        kegiatanList.removeAt(position)
        kegiatanAdapter.notifyItemRemoved(position)

        Toast.makeText(
            this,
            "Kegiatan ${kegiatan.namaKegiatan} telah dihapus",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sortList() {
        kegiatanList.sortWith(compareBy<Kegiatan> { it.selesai }.thenBy { it.targetSelesai })
    }
}