# xammp-PHP-write-AndroidApp

xammp

connection.php

<?php
$servername = "localhost";
$username = "root";
$password = ""; 
$dbname = "database name";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Database connection failed: " . $conn->connect_error);
}
?>

indexwrite.php

<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "database name";

$courseNumber = str_replace(' ', '_', $_POST['course_number']); 
$studentName = $_POST['student_name'];
$attendance = $_POST['attendance'];

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Veritabanına bağlanılamadı: " . $conn->connect_error);
}

$sql = "SELECT * FROM $courseNumber WHERE student_name='$studentName'"; 
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $existingAttendance = $row['attendance'];
    $newAttendance = $existingAttendance . "; " . $attendance;

    $updateSql = "UPDATE $courseNumber SET attendance='$newAttendance' WHERE student_name='$studentName'"; 
    if ($conn->query($updateSql) === TRUE) {
        echo "Yoklama bilgileri güncellendi";
    } else {
        echo "Hata: " . $conn->error;
    }
} else {
    $insertSql = "INSERT INTO $courseNumber (student_name, attendance) VALUES ('$studentName', '$attendance')";
    if ($conn->query($insertSql) === TRUE) {
        echo "Yoklama bilgileri başarıyla eklendi";
    } else {
        echo "Hata: " . $conn->error;
    }
}

$conn->close();
?>

indexread.php

<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "database name";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Veritabanı bağlantısı başarısız oldu: " . $conn->connect_error);
}

$courseList = array("Course_1", "Course_2", "Course_3");

foreach ($courseList as $course) {
    $sql = "SELECT * FROM $course";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $key = $row["key1"];
            $studentName = $row["student_name"];
            $attendance = $row["attendance"];

            echo "Key: $key - Ders: $course - Öğrenci Adı: $studentName - Yoklama: $attendance<br>";
        }
    } else {
        echo "$course dersi için yoklama bilgisi bulunamadı<br>";
    }
}

$conn->close();
?>

