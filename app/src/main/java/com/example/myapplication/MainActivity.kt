package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCourseNumber: Spinner
    private lateinit var etStudentName: EditText
    private lateinit var etAttendance: EditText
    private lateinit var btnSendAttendance: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCourseNumber = findViewById(R.id.spinnerCourseNumber)
        etStudentName = findViewById(R.id.editTextStudentName)
        etAttendance = findViewById(R.id.editTextAttendance)
        btnSendAttendance = findViewById(R.id.buttonSubmit)

        // Spinner için bir ArrayAdapter oluşturun
        ArrayAdapter.createFromResource(
            this,
            R.array.course_array, // Derslerin listesi bir dizi kaynağından alınır
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCourseNumber.adapter = adapter
        }

        btnSendAttendance.setOnClickListener {
            val courseNumber = spinnerCourseNumber.selectedItem.toString()
            val studentName = etStudentName.text.toString()
            val attendance = etAttendance.text.toString()

            val sendAttendanceTask = SendAttendanceTask()
            sendAttendanceTask.execute(courseNumber, studentName, attendance)
        }
    }

    inner class SendAttendanceTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            val courseNumber = params[0]
            val studentName = params[1]
            val attendance = params[2]

            val url = URL("your link")

            val urlConnection = url.openConnection() as HttpURLConnection

            try {
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true

                val postData = "course_number=$courseNumber&student_name=$studentName&attendance=$attendance"
                val outputBytes = postData.toByteArray(Charsets.UTF_8)

                urlConnection.setRequestProperty("Content-Length", outputBytes.size.toString())
                urlConnection.outputStream.write(outputBytes)

                Log.d("SendAttendanceTask", "HTTP POST isteği gönderildi: $postData") // Log mesajını buraya ekleyin

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    bufferedReader.close()

                    return response.toString()
                }


            } catch (e: Exception) {
                Log.e("SendAttendanceTask", "Error: ${e.message}")
            } finally {
                urlConnection.disconnect()
            }

            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                Log.d("SendAttendanceTask", "Sonuç: $result")
                Toast.makeText(this@MainActivity, "Yoklama bilgileri başarıyla gönderildi.", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("SendAttendanceTask", "Yoklama bilgileri gönderilirken bir hata oluştu.")
            }
        }
    }
}
