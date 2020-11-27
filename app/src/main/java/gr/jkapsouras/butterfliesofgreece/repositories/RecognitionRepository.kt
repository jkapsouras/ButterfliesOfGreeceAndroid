package gr.jkapsouras.butterfliesofgreece.repositories

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.dto.Avatar
import gr.jkapsouras.butterfliesofgreece.dto.BAvatar
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import gr.jkapsouras.butterfliesofgreece.dto.Predictions
import gr.jkapsouras.butterfliesofgreece.managers.recognition.RecognitionManager
import gr.jkapsouras.butterfliesofgreece.network.IImageApi
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.*


class RecognitionRepository(private val api: IImageApi) {

    lateinit var activity: MainActivity

      fun recognize(image: Avatar) : Observable<Predictions>{

          val file = File(getRealPathFromURI(image.uri))


          val bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image.uri)

          val newbitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false)

          val baos = ByteArrayOutputStream()
          newbitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
          val bitmapdata: ByteArray = baos.toByteArray()
          val compressedBitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.count())
          var fileName:String = "avatar"
          val leftImageFile = convertBitmapToFile(fileName, compressedBitmap)
          //creating request body for file

          val requestFile =
              RequestBody.create(MediaType.parse(activity.contentResolver.getType(image.uri)), leftImageFile)

        return api.uploadImage(requestFile)//, descBody)
    }

    fun recognize(image: BAvatar) : Observable<Predictions>{

        val newbitmap = Bitmap.createScaledBitmap(image.image, 600, 600, false)

        val baos = ByteArrayOutputStream()
        newbitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val bitmapdata: ByteArray = baos.toByteArray()
        val compressedBitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.count())
        var fileName:String = "avatar"
        val leftImageFile = convertBitmapToFile(fileName, compressedBitmap)
        //creating request body for file

        val requestFile =
            RequestBody.create(MediaType.parse("image/*"), leftImageFile)

        return api.uploadImage(requestFile)//, descBody)
    }

    fun offlineRecognize(avatar: Avatar) : Observable<List<Prediction>>{
        val bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), avatar.uri)
        val newbitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, false)
        val recognizeManager = RecognitionManager(bitmap = newbitmap, activity)
        return Observable.just(recognizeManager.recognize())
    }

    fun offlineRecognize(bitmap: BAvatar) : Observable<List<Prediction>>{
        val newbitmap = Bitmap.createScaledBitmap(bitmap.image, 600, 600, false)
        val recognizeManager = RecognitionManager(bitmap = newbitmap, activity)
        return Observable.just(recognizeManager.recognize())
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(activity, contentUri, proj, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(activity.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}