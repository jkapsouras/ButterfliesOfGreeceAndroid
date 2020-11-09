package gr.jkapsouras.butterfliesofgreece

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import java.io.File

//class FileSelectActivity : Activity() {
//
//    // The path to the root of this app's internal storage
//    private lateinit var privateRootDir: File
//    // The path to the "images" subdirectory
//    private lateinit var imagesDir: File
//    // Array of files in the images subdirectory
//    private lateinit var imageFiles: Array<File>
//    // Array of filenames corresponding to imageFiles
//    private lateinit var imageFilenames: Array<String>
//
//    // Initialize the Activity
//    override fun onCreate(savedInstanceState: Bundle?) {
//        // Set up an Intent to send back to apps that request a file
//        resultIntent = Intent("com.example.myapp.ACTION_RETURN_FILE")
//        // Get the files/ subdirectory of internal storage
//        privateRootDir = filesDir
//        // Get the files/images subdirectory;
//        imagesDir = File(privateRootDir, "images")
//        // Get the files in the images subdirectory
//        imageFiles = imagesDir.listFiles()
//        // Set the Activity's result to null to begin with
//        setResult(Activity.RESULT_CANCELED, null)
//        /*
//         * Display the file names in the ListView fileListView.
//         * Back the ListView with the array imageFilenames, which
//         * you can create by iterating through imageFiles and
//         * calling File.getAbsolutePath() for each File
//         */
//    }
//}