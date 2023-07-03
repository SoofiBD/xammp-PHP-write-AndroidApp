import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "onvural.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Her ders için ayrı bir tablo oluşturun
        val courseList = listOf("Course_1", "Course_2", "Course_3")
        for (course in courseList) {
            val createTableQuery = "CREATE TABLE IF NOT EXISTS $course (key1 INTEGER PRIMARY KEY AUTOINCREMENT, student_name TEXT, attendance TEXT)"
            db.execSQL(createTableQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Her ders için ayrı bir tablo oluşturun
        val courseList = listOf("Course_1", "Course_2", "Course_3")
        for (course in courseList) {
            val dropTableQuery = "DROP TABLE IF EXISTS $course"
            db.execSQL(dropTableQuery)
        }
        onCreate(db)
    }

    fun insertAttendance(course: String, studentName: String, attendance: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("student_name", studentName)
        contentValues.put("attendance", attendance)
        return db.insert(course, null, contentValues)
    }
}
