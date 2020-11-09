package gr.jkapsouras.butterfliesofgreece.managers

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.util.Log
import android.view.View
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.state.PdfArrange
import org.koin.dsl.koinApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PdfManager{
    private var document: PdfDocument = PdfDocument()
    private val pageWidth = 8.5 * 72.0
    private val pageHeight = 11 * 72.0

     fun createPdf(view: View, context: Context, photos: List<ButterflyPhoto>, pdfArrange: PdfArrange) : String {

         val pageInfo = PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), 1).create()

         when (pdfArrange){
             PdfArrange.OnePerPage ->{
                 for(photo in photos){
                     val page = addOneImageWithText(view, photo, pageInfo)
                     document.finishPage(page)
                 }
             }
             PdfArrange.TwoPerPage->{
                 for (i in 0..photos.count() step 2){
                     if (i >= photos.count()){
                         break
                     }
                     if (i+1 >= photos.count()){
                         break
                     }
                     val page = addTwoImagesWithText(view, photos[i], photos[i+1], pageInfo)
                     document.finishPage(page)
                 }
             }
             PdfArrange.FourPerPage ->{
                 for (i in 0..photos.count() step 4){
                     if (i >= photos.count()){
                         break
                     }
                     if (i+1 >= photos.count()){
                         break
                     }
                     if (i+2 >= photos.count()){
                         break
                     }
                     if (i+3 >= photos.count()){
                         break
                     }
                     val page = addFourImagesWithText(view, photos[i], photos[i+1],photos[i+2], photos[i+3], pageInfo)
                     document.finishPage(page)
                 }
             }
             PdfArrange.SixPerPage ->{
                 for (i in 0..photos.count() step 6){
                     if (i >= photos.count()){
                         break
                     }
                     if (i+1 >= photos.count()){
                         break
                     }
                     if (i+2 >= photos.count()){
                         break
                     }
                     if (i+3 >= photos.count()){
                         break
                     }
                     if (i+4 >= photos.count()){
                         break
                     }
                     if (i+5 >= photos.count()){
                         break
                     }
                     val page = addSixImagesWithText(view, photos[i], photos[i+1],photos[i+2], photos[i+3],photos[i+4],photos[i+5], pageInfo)
                     document.finishPage(page)
                 }
             }
         }

         Log.d(TAG, "before writeTo")

         val folder = context.filesDir
         val f = File(folder, "images")
         f.mkdir()

         val file = File("${context.filesDir}/images/temp.pdf")
         val fos: FileOutputStream
         try {

             fos = FileOutputStream(file)
             document.writeTo(fos)
             document.close()
             fos.close()
         } catch (e: IOException) {
             e.printStackTrace()
         }
         return "${context.filesDir}/images/temp.pdf"
     }

    private fun addOneImageWithText(view: View, photo: ButterflyPhoto, pageInfo: PageInfo) : PdfDocument.Page{

        val page: PdfDocument.Page = document.startPage(pageInfo)
        val firstLineText = photo.author
        val secondLineText=photo.specieName

        val textPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field_dark)
                style = Paint.Style.FILL
                textSize = view.resources.getDimension(R.dimen.photos)
            }

        val rectPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field)
                style = Paint.Style.FILL
            }

        val firstLineBounds = Rect()
        textPaint.getTextBounds(firstLineText, 0, firstLineText.length, firstLineBounds)

        val secondLineBounds = Rect()
        textPaint.getTextBounds(secondLineText, 0, secondLineText.length, secondLineBounds)
        var b:Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val scb = scale(b, pageWidth.toInt(), pageHeight.toInt())
        val imageRect = Rect(0, 0, scb.first, scb.second)
        page.canvas.drawBitmap(b, null, imageRect, null)

        b.recycle()

        val backgroundRect =  Rect(
            imageRect.left,
            imageRect.height(),
            imageRect.left + imageRect.width(),
            imageRect.height() + firstLineBounds.height() + secondLineBounds.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect, rectPaint
        )

        page.canvas.drawText(
            firstLineText,
            firstLineBounds.left.toFloat(),
            backgroundRect.top.toFloat() + firstLineBounds.height() + 8,
            textPaint
        )

         page.canvas.drawText(
             secondLineText,
             secondLineBounds.left.toFloat(),
             backgroundRect.top.toFloat() + firstLineBounds.height() + secondLineBounds.height() + 8 + 8,
             textPaint
         )
        return page
    }

    private fun addTwoImagesWithText(view: View, photo1: ButterflyPhoto,photo2: ButterflyPhoto, pageInfo: PageInfo) : PdfDocument.Page {

        val textPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field_dark)
                style = Paint.Style.FILL
                textSize = view.resources.getDimension(R.dimen.added_photos)
            }

        val rectPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field)
                style = Paint.Style.FILL
            }

        val page: PdfDocument.Page = document.startPage(pageInfo)

        //-------------first image----------------

        val firstLineText1 = photo1.author
        val secondLineText1 = photo1.specieName

        val firstLineBounds1 = Rect()
        textPaint.getTextBounds(firstLineText1, 0, firstLineText1.length, firstLineBounds1)

        val secondLineBounds1 = Rect()
        textPaint.getTextBounds(secondLineText1, 0, secondLineText1.length, secondLineBounds1)

        var b1: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo1.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b1 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b1 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight1 =
            ((pageHeight * 0.9) - ((2 * firstLineBounds1.height() + 2 * secondLineBounds1.height()))) / 2
        val maxWidth1 = pageWidth * 0.9

        val scb1 = scale(b1, maxWidth1.toInt(), maxHeight1.toInt())
        Log.d(TAG, "bitmap size: ${scb1.first} ${scb1.second} page size: $pageWidth $pageHeight")

        val image1X = (pageWidth - scb1.first) / 2.0
        val imageRect1 = Rect(image1X.toInt(), 0, scb1.first + image1X.toInt(), scb1.second)
        page.canvas.drawBitmap(b1, null, imageRect1, null)

        b1.recycle()

        val backgroundRect1 = Rect(
            imageRect1.left,
            imageRect1.height(),
            imageRect1.left + imageRect1.width(),
            imageRect1.height() + firstLineBounds1.height() + secondLineBounds1.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect1, rectPaint
        )

        val firstTextTop1 = imageRect1.top + imageRect1.height() + firstLineBounds1.height() + 4

        val firstTextRect1 = Rect(
            imageRect1.left + 4,
            firstTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            firstLineText1,
            firstTextRect1.left.toFloat(),
            firstTextRect1.top.toFloat(),
            textPaint
        )

        val secondTextTop1 = firstTextTop1 + 8 + secondLineBounds1.height()

        val secondTextRect1 = Rect(
            imageRect1.left + 4,
            secondTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            secondLineText1,
            secondTextRect1.left.toFloat(),
            secondTextRect1.top.toFloat(),
            textPaint
        )

        //-------------second image----------------
        val firstLineText2 = photo2.author
        val secondLineText2 = photo2.specieName

        val firstLineBounds2 = Rect()
        textPaint.getTextBounds(firstLineText2, 0, firstLineText2.length, firstLineBounds2)

        val secondLineBounds2 = Rect()
        textPaint.getTextBounds(secondLineText2, 0, secondLineText2.length, secondLineBounds2)

        var b2: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo2.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b2 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b2 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight2 =
            ((pageHeight * 0.9) - ((2 * firstLineBounds2.height() + 2 * secondLineBounds2.height()))) / 2
        val maxWidth2 = pageWidth * 0.9

        val scb2 = scale(b2, maxWidth2.toInt(), maxHeight2.toInt())
        Log.d(TAG, "bitmap size: ${scb2.first} ${scb2.second} page size: $pageWidth $pageHeight")

        val image2X = (pageWidth - scb2.first) / 2.0
        val imageRect2 = Rect(
            image2X.toInt(),
            (pageHeight / 2).toInt(),
            scb2.first + image2X.toInt(),
            scb2.second + (pageHeight / 2).toInt()
        )
        page.canvas.drawBitmap(b2, null, imageRect2, null)

        b2.recycle()

        val backgroundRect2 = Rect(
            imageRect2.left,
            imageRect2.top + imageRect2.height(),
            imageRect2.left + imageRect2.width(),
            imageRect2.top +imageRect2.height() + firstLineBounds2.height() + secondLineBounds2.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect2, rectPaint
        )

        val firstTextTop2 = imageRect2.top + imageRect2.height() + firstLineBounds2.height() + 4

        val firstTextRect2 = Rect(
            imageRect2.left + 4,
            firstTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            firstLineText2,
            firstTextRect2.left.toFloat(),
            firstTextRect2.top.toFloat(),
            textPaint
        )

        val secondTextTop2 = firstTextTop2 + 8 + secondLineBounds2.height()

        val secondTextRect2 = Rect(
            imageRect2.left + 4,
            secondTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            secondLineText2,
            secondTextRect2.left.toFloat(),
            secondTextRect2.top.toFloat(),
            textPaint
        )

        return page
    }

    private fun addFourImagesWithText(view: View, photo1: ButterflyPhoto,photo2: ButterflyPhoto,photo3: ButterflyPhoto,photo4: ButterflyPhoto, pageInfo: PageInfo) : PdfDocument.Page {

        val textPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field_dark)
                style = Paint.Style.FILL
                textSize = view.resources.getDimension(R.dimen.added_photos)
            }

        val rectPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field)
                style = Paint.Style.FILL
            }

        val page: PdfDocument.Page = document.startPage(pageInfo)

        //-------------first image----------------

        val firstLineText1 = photo1.author
        val secondLineText1 = photo1.specieName

        val firstLineBounds1 = Rect()
        textPaint.getTextBounds(firstLineText1, 0, firstLineText1.length, firstLineBounds1)

        val secondLineBounds1 = Rect()
        textPaint.getTextBounds(secondLineText1, 0, secondLineText1.length, secondLineBounds1)

        var b1: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo1.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b1 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b1 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight1 = ((pageHeight)-((2 * firstLineBounds1.height() + 2 * secondLineBounds1.height()))) / 2
        val maxWidth1 = (pageWidth)/2

        val scb1 = scale(b1, maxWidth1.toInt(), maxHeight1.toInt())
        Log.d(TAG, "bitmap size: ${scb1.first} ${scb1.second} page size: $pageWidth $pageHeight")

        val image1X = 0
        val imageRect1 = Rect(image1X, 0, scb1.first + image1X, scb1.second)
        page.canvas.drawBitmap(b1, null, imageRect1, null)

        b1.recycle()

        val backgroundRect1 = Rect(
            imageRect1.left,
            imageRect1.height(),
            imageRect1.left + imageRect1.width(),
            imageRect1.height() + firstLineBounds1.height() + secondLineBounds1.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect1, rectPaint
        )

        val firstTextTop1 = imageRect1.top + imageRect1.height() + firstLineBounds1.height() + 4

        val firstTextRect1 = Rect(
            imageRect1.left + 4,
            firstTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            firstLineText1,
            firstTextRect1.left.toFloat(),
            firstTextRect1.top.toFloat(),
            textPaint
        )

        val secondTextTop1 = firstTextTop1 + 8 + secondLineBounds1.height()

        val secondTextRect1 = Rect(
            imageRect1.left + 4,
            secondTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            secondLineText1,
            secondTextRect1.left.toFloat(),
            secondTextRect1.top.toFloat(),
            textPaint
        )

        //-------------second image----------------
        val firstLineText2 = photo2.author
        val secondLineText2 = photo2.specieName

        val firstLineBounds2 = Rect()
        textPaint.getTextBounds(firstLineText2, 0, firstLineText2.length, firstLineBounds2)

        val secondLineBounds2 = Rect()
        textPaint.getTextBounds(secondLineText2, 0, secondLineText2.length, secondLineBounds2)

        var b2: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo2.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b2 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b2 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight2 = ((pageHeight)-((2 * firstLineBounds2.height() + 2 * secondLineBounds2.height()))) / 2
        val maxWidth2 = (pageWidth)/2

        val scb2 = scale(b2, maxWidth2.toInt(), maxHeight2.toInt())
        Log.d(TAG, "bitmap size: ${scb2.first} ${scb2.second} page size: $pageWidth $pageHeight")

        val image2X = imageRect1.right
        val imageRect2 = Rect(
            image2X,
            imageRect1.top,
            scb2.first + image2X,
            scb2.second + imageRect1.top
        )
        page.canvas.drawBitmap(b2, null, imageRect2, null)

        b2.recycle()

        val backgroundRect2 = Rect(
            imageRect2.left,
            imageRect2.top + imageRect2.height(),
            imageRect2.left + imageRect2.width(),
            imageRect2.top +imageRect2.height() + firstLineBounds2.height() + secondLineBounds2.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect2, rectPaint
        )

        val firstTextTop2 = imageRect2.top + imageRect2.height() + firstLineBounds2.height() + 4

        val firstTextRect2 = Rect(
            imageRect2.left + 4,
            firstTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            firstLineText2,
            firstTextRect2.left.toFloat(),
            firstTextRect2.top.toFloat(),
            textPaint
        )

        val secondTextTop2 = firstTextTop2 + 8 + secondLineBounds2.height()

        val secondTextRect2 = Rect(
            imageRect2.left + 4,
            secondTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            secondLineText2,
            secondTextRect2.left.toFloat(),
            secondTextRect2.top.toFloat(),
            textPaint
        )

         //-------------third image----------------
        val firstLineText3 = photo3.author
        val secondLineText3 = photo3.specieName

        val firstLineBounds3 = Rect()
        textPaint.getTextBounds(firstLineText3, 0, firstLineText3.length, firstLineBounds3)

        val secondLineBounds3 = Rect()
        textPaint.getTextBounds(secondLineText3, 0, secondLineText3.length, secondLineBounds3)

        var b3: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo3.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b3 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b3 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight3 = ((pageHeight)-((2 * firstLineBounds3.height() + 2 * secondLineBounds3.height()))) / 2
        val maxWidth3 = (pageWidth)/2

        val scb3 = scale(b3, maxWidth3.toInt(), maxHeight3.toInt())
        Log.d(TAG, "bitmap size: ${scb3.first} ${scb3.second} page size: $pageWidth $pageHeight")

        val image3X = imageRect1.left
        val imageRect3 = Rect(
            image3X,
            (pageHeight/2).toInt(),
            scb3.first + image3X,
            scb3.second + (pageHeight/2).toInt()
        )
        page.canvas.drawBitmap(b3, null, imageRect3, null)

        b3.recycle()

        val backgroundRect3 = Rect(
            imageRect3.left,
            imageRect3.top + imageRect3.height(),
            imageRect3.left + imageRect3.width(),
            imageRect3.top +imageRect3.height() + firstLineBounds3.height() + secondLineBounds3.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect3, rectPaint
        )

        val firstTextTop3 = imageRect3.top + imageRect3.height() + firstLineBounds3.height() + 4

        val firstTextRect3 = Rect(
            imageRect3.left + 4,
            firstTextTop3,
            scb3.first - 4,
            scb3.second
        )

        page.canvas.drawText(
            firstLineText3,
            firstTextRect3.left.toFloat(),
            firstTextRect3.top.toFloat(),
            textPaint
        )

        val secondTextTop3 = firstTextTop3 + 8 + secondLineBounds3.height()

        val secondTextRect3 = Rect(
            imageRect3.left + 4,
            secondTextTop3,
            scb3.first - 4,
            scb3.second
        )

        page.canvas.drawText(
            secondLineText3,
            secondTextRect3.left.toFloat(),
            secondTextRect3.top.toFloat(),
            textPaint
        )

         //-------------fourth image----------------
        val firstLineText4 = photo4.author
        val secondLineText4 = photo4.specieName

        val firstLineBounds4 = Rect()
        textPaint.getTextBounds(firstLineText4, 0, firstLineText4.length, firstLineBounds4)

        val secondLineBounds4 = Rect()
        textPaint.getTextBounds(secondLineText4, 0, secondLineText4.length, secondLineBounds4)

        var b4: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo4.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b4 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b4 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight4 = ((pageHeight)-((2 * firstLineBounds4.height() + 2 * secondLineBounds4.height()))) / 2
        val maxWidth4 = (pageWidth)/2

        val scb4 = scale(b4, maxWidth4.toInt(), maxHeight4.toInt())
        Log.d(TAG, "bitmap size: ${scb4.first} ${scb4.second} page size: $pageWidth $pageHeight")

        val image4X = imageRect1.left + imageRect1.width()
        val imageRect4 = Rect(
            image4X,
            (pageHeight/2).toInt(),
            scb4.first + image4X,
            scb4.second + (pageHeight/2).toInt()
        )
        page.canvas.drawBitmap(b4, null, imageRect4, null)

        b4.recycle()

        val backgroundRect4 = Rect(
            imageRect4.left,
            imageRect4.top + imageRect4.height(),
            imageRect4.left + imageRect4.width(),
            imageRect4.top +imageRect4.height() + firstLineBounds4.height() + secondLineBounds4.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect4, rectPaint
        )

        val firstTextTop4 = imageRect4.top + imageRect4.height() + firstLineBounds4.height() + 4

        val firstTextRect4 = Rect(
            imageRect4.left + 4,
            firstTextTop4,
            scb4.first - 4,
            scb4.second
        )

        page.canvas.drawText(
            firstLineText4,
            firstTextRect4.left.toFloat(),
            firstTextRect4.top.toFloat(),
            textPaint
        )

        val secondTextTop4 = firstTextTop4 + 8 + secondLineBounds4.height()

        val secondTextRect4 = Rect(
            imageRect4.left + 4,
            secondTextTop4,
            scb4.first - 4,
            scb4.second
        )

        page.canvas.drawText(
            secondLineText4,
            secondTextRect4.left.toFloat(),
            secondTextRect4.top.toFloat(),
            textPaint
        )

        return page
    }

    private fun addSixImagesWithText(view: View, photo1: ButterflyPhoto,photo2: ButterflyPhoto,photo3: ButterflyPhoto,photo4: ButterflyPhoto,photo5: ButterflyPhoto,photo6: ButterflyPhoto, pageInfo: PageInfo) : PdfDocument.Page {

        val textPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field_dark)
                style = Paint.Style.FILL
                textSize = view.resources.getDimension(R.dimen.added_photos)
            }

        val rectPaint =
            Paint().apply {
                isAntiAlias = true
                color = view.context.getColor(R.color.field)
                style = Paint.Style.FILL
            }

        val page: PdfDocument.Page = document.startPage(pageInfo)

        //-------------first image----------------

        val firstLineText1 = photo1.author
        val secondLineText1 = photo1.specieName

        val firstLineBounds1 = Rect()
        textPaint.getTextBounds(firstLineText1, 0, firstLineText1.length, firstLineBounds1)

        val secondLineBounds1 = Rect()
        textPaint.getTextBounds(secondLineText1, 0, secondLineText1.length, secondLineBounds1)

        var b1: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo1.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b1 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b1 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight1 = ((pageHeight)-((2 * firstLineBounds1.height() + 2 * secondLineBounds1.height()))) / 2
        val maxWidth1 = (pageWidth)/3

        val scb1 = scale(b1, maxWidth1.toInt(), maxHeight1.toInt())
        Log.d(TAG, "bitmap size: ${scb1.first} ${scb1.second} page size: $pageWidth $pageHeight")

        val image1X = 0
        val imageRect1 = Rect(image1X, 0, scb1.first + image1X, scb1.second)
        page.canvas.drawBitmap(b1, null, imageRect1, null)

        b1.recycle()

        val backgroundRect1 = Rect(
            imageRect1.left,
            imageRect1.height(),
            imageRect1.left + imageRect1.width(),
            imageRect1.height() + firstLineBounds1.height() + secondLineBounds1.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect1, rectPaint
        )

        val firstTextTop1 = imageRect1.top + imageRect1.height() + firstLineBounds1.height() + 4

        val firstTextRect1 = Rect(
            imageRect1.left + 4,
            firstTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            firstLineText1,
            firstTextRect1.left.toFloat(),
            firstTextRect1.top.toFloat(),
            textPaint
        )

        val secondTextTop1 = firstTextTop1 + 8 + secondLineBounds1.height()

        val secondTextRect1 = Rect(
            imageRect1.left + 4,
            secondTextTop1,
            scb1.first - 4,
            scb1.second
        )

        page.canvas.drawText(
            secondLineText1,
            secondTextRect1.left.toFloat(),
            secondTextRect1.top.toFloat(),
            textPaint
        )

        //-------------second image----------------
        val firstLineText2 = photo2.author
        val secondLineText2 = photo2.specieName

        val firstLineBounds2 = Rect()
        textPaint.getTextBounds(firstLineText2, 0, firstLineText2.length, firstLineBounds2)

        val secondLineBounds2 = Rect()
        textPaint.getTextBounds(secondLineText2, 0, secondLineText2.length, secondLineBounds2)

        var b2: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo2.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b2 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b2 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight2 = ((pageHeight)-((2 * firstLineBounds2.height() + 2 * secondLineBounds2.height()))) / 2
        val maxWidth2 = (pageWidth)/3

        val scb2 = scale(b2, maxWidth2.toInt(), maxHeight2.toInt())
        Log.d(TAG, "bitmap size: ${scb2.first} ${scb2.second} page size: $pageWidth $pageHeight")

        val image2X = imageRect1.right
        val imageRect2 = Rect(
            image2X,
            imageRect1.top,
            scb2.first + image2X,
            scb2.second + imageRect1.top
        )
        page.canvas.drawBitmap(b2, null, imageRect2, null)

        b2.recycle()

        val backgroundRect2 = Rect(
            imageRect2.left,
            imageRect2.top + imageRect2.height(),
            imageRect2.left + imageRect2.width(),
            imageRect2.top +imageRect2.height() + firstLineBounds2.height() + secondLineBounds2.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect2, rectPaint
        )

        val firstTextTop2 = imageRect2.top + imageRect2.height() + firstLineBounds2.height() + 4

        val firstTextRect2 = Rect(
            imageRect2.left + 4,
            firstTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            firstLineText2,
            firstTextRect2.left.toFloat(),
            firstTextRect2.top.toFloat(),
            textPaint
        )

        val secondTextTop2 = firstTextTop2 + 8 + secondLineBounds2.height()

        val secondTextRect2 = Rect(
            imageRect2.left + 4,
            secondTextTop2,
            scb2.first - 4,
            scb2.second
        )

        page.canvas.drawText(
            secondLineText2,
            secondTextRect2.left.toFloat(),
            secondTextRect2.top.toFloat(),
            textPaint
        )

        //-------------third image----------------
        val firstLineText3 = photo3.author
        val secondLineText3 = photo3.specieName

        val firstLineBounds3 = Rect()
        textPaint.getTextBounds(firstLineText3, 0, firstLineText3.length, firstLineBounds3)

        val secondLineBounds3 = Rect()
        textPaint.getTextBounds(secondLineText3, 0, secondLineText3.length, secondLineBounds3)

        var b3: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo3.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b3 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b3 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight3 = ((pageHeight)-((2 * firstLineBounds3.height() + 2 * secondLineBounds3.height()))) / 2
        val maxWidth3 = (pageWidth)/3

        val scb3 = scale(b3, maxWidth3.toInt(), maxHeight3.toInt())
        Log.d(TAG, "bitmap size: ${scb3.first} ${scb3.second} page size: $pageWidth $pageHeight")

        val image3X = imageRect2.right
        val imageRect3 = Rect(
            image3X,
            imageRect1.top,
            scb3.first + image3X,
            scb3.second + imageRect1.top
        )
        page.canvas.drawBitmap(b3, null, imageRect3, null)

        b3.recycle()

        val backgroundRect3 = Rect(
            imageRect3.left,
            imageRect3.top + imageRect3.height(),
            imageRect3.left + imageRect3.width(),
            imageRect3.top +imageRect3.height() + firstLineBounds3.height() + secondLineBounds3.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect3, rectPaint
        )

        val firstTextTop3 = imageRect3.top + imageRect3.height() + firstLineBounds3.height() + 4

        val firstTextRect3 = Rect(
            imageRect3.left + 4,
            firstTextTop3,
            scb3.first - 4,
            scb3.second
        )

        page.canvas.drawText(
            firstLineText3,
            firstTextRect3.left.toFloat(),
            firstTextRect3.top.toFloat(),
            textPaint
        )

        val secondTextTop3 = firstTextTop3 + 8 + secondLineBounds3.height()

        val secondTextRect3 = Rect(
            imageRect3.left + 4,
            secondTextTop3,
            scb3.first - 4,
            scb3.second
        )

        page.canvas.drawText(
            secondLineText3,
            secondTextRect3.left.toFloat(),
            secondTextRect3.top.toFloat(),
            textPaint
        )

        //-------------fourth image----------------
        val firstLineText4 = photo4.author
        val secondLineText4 = photo4.specieName

        val firstLineBounds4 = Rect()
        textPaint.getTextBounds(firstLineText4, 0, firstLineText4.length, firstLineBounds4)

        val secondLineBounds4 = Rect()
        textPaint.getTextBounds(secondLineText4, 0, secondLineText4.length, secondLineBounds4)

        var b4: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo4.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b4 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b4 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight4 = ((pageHeight)-((2 * firstLineBounds4.height() + 2 * secondLineBounds4.height()))) / 2
        val maxWidth4 = (pageWidth)/3

        val scb4 = scale(b4, maxWidth4.toInt(), maxHeight4.toInt())
        Log.d(TAG, "bitmap size: ${scb4.first} ${scb4.second} page size: $pageWidth $pageHeight")

        val image4X = imageRect1.left
        val imageRect4 = Rect(
            image4X,
            (pageHeight/2).toInt(),
            scb4.first + image4X,
            scb4.second + (pageHeight/2).toInt()
        )
        page.canvas.drawBitmap(b4, null, imageRect4, null)

        b4.recycle()

        val backgroundRect4 = Rect(
            imageRect4.left,
            imageRect4.top + imageRect4.height(),
            imageRect4.left + imageRect4.width(),
            imageRect4.top +imageRect4.height() + firstLineBounds4.height() + secondLineBounds4.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect4, rectPaint
        )

        val firstTextTop4 = imageRect4.top + imageRect4.height() + firstLineBounds4.height() + 4

        val firstTextRect4 = Rect(
            imageRect4.left + 4,
            firstTextTop4,
            scb4.first - 4,
            scb4.second
        )

        page.canvas.drawText(
            firstLineText4,
            firstTextRect4.left.toFloat(),
            firstTextRect4.top.toFloat(),
            textPaint
        )

        val secondTextTop4 = firstTextTop4 + 8 + secondLineBounds4.height()

        val secondTextRect4 = Rect(
            imageRect4.left + 4,
            secondTextTop4,
            scb4.first - 4,
            scb4.second
        )

        page.canvas.drawText(
            secondLineText4,
            secondTextRect4.left.toFloat(),
            secondTextRect4.top.toFloat(),
            textPaint
        )

        //-------------fifth image----------------
        val firstLineText5 = photo5.author
        val secondLineText5 = photo5.specieName

        val firstLineBounds5 = Rect()
        textPaint.getTextBounds(firstLineText5, 0, firstLineText5.length, firstLineBounds5)

        val secondLineBounds5 = Rect()
        textPaint.getTextBounds(secondLineText5, 0, secondLineText5.length, secondLineBounds5)

        var b5: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo5.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b5 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b5 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight5 = ((pageHeight)-((2 * firstLineBounds5.height() + 2 * secondLineBounds5.height()))) / 2
        val maxWidth5 = (pageWidth)/3

        val scb5 = scale(b5, maxWidth5.toInt(), maxHeight5.toInt())
        Log.d(TAG, "bitmap size: ${scb5.first} ${scb5.second} page size: $pageWidth $pageHeight")

        val image5X = imageRect1.right
        val imageRect5 = Rect(
            image5X,
            (pageHeight/2).toInt(),
            scb5.first + image5X,
            scb5.second + (pageHeight/2).toInt()
        )
        page.canvas.drawBitmap(b5, null, imageRect5, null)

        b5.recycle()

        val backgroundRect5 = Rect(
            imageRect5.left,
            imageRect5.top + imageRect5.height(),
            imageRect5.left + imageRect5.width(),
            imageRect5.top +imageRect5.height() + firstLineBounds5.height() + secondLineBounds5.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect5, rectPaint
        )

        val firstTextTop5 = imageRect5.top + imageRect5.height() + firstLineBounds5.height() + 4

        val firstTextRect5 = Rect(
            imageRect5.left + 4,
            firstTextTop5,
            scb5.first - 4,
            scb5.second
        )

        page.canvas.drawText(
            firstLineText5,
            firstTextRect5.left.toFloat(),
            firstTextRect5.top.toFloat(),
            textPaint
        )

        val secondTextTop5 = firstTextTop5 + 8 + secondLineBounds5.height()

        val secondTextRect5 = Rect(
            imageRect5.left + 4,
            secondTextTop5,
            scb5.first - 4,
            scb5.second
        )

        page.canvas.drawText(
            secondLineText5,
            secondTextRect5.left.toFloat(),
            secondTextRect5.top.toFloat(),
            textPaint
        )

        //-------------sixth image----------------
        val firstLineText6 = photo6.author
        val secondLineText6 = photo6.specieName

        val firstLineBounds6 = Rect()
        textPaint.getTextBounds(firstLineText6, 0, firstLineText6.length, firstLineBounds6)

        val secondLineBounds6 = Rect()
        textPaint.getTextBounds(secondLineText6, 0, secondLineText6.length, secondLineBounds6)

        var b6: Bitmap
        try {
            val iden = view.resources.getIdentifier(
                "thumb_big_${photo6.source}",
                "drawable",
                view.context.packageName
            )
            iden.let { tmpiden ->
                b6 = BitmapFactory.decodeResource(view.resources, tmpiden)
            }
        } catch (e: Exception) {
            b6 = BitmapFactory.decodeResource(view.resources, R.drawable.full_default)
        }

        val maxHeight6 = ((pageHeight)-((2 * firstLineBounds6.height() + 2 * secondLineBounds6.height()))) / 2
        val maxWidth6 = (pageWidth)/3

        val scb6 = scale(b6, maxWidth6.toInt(), maxHeight6.toInt())
        Log.d(TAG, "bitmap size: ${scb6.first} ${scb6.second} page size: $pageWidth $pageHeight")

        val image6X = imageRect2.right
        val imageRect6 = Rect(
            image6X,
            (pageHeight/2).toInt(),
            scb6.first + image6X,
            scb6.second + (pageHeight/2).toInt()
        )
        page.canvas.drawBitmap(b6, null, imageRect6, null)

        b6.recycle()

        val backgroundRect6 = Rect(
            imageRect6.left,
            imageRect6.top + imageRect6.height(),
            imageRect6.left + imageRect6.width(),
            imageRect6.top +imageRect6.height() + firstLineBounds6.height() + secondLineBounds6.height() + 32
        )

        page.canvas.drawRect(
            backgroundRect6, rectPaint
        )

        val firstTextTop6 = imageRect6.top + imageRect6.height() + firstLineBounds6.height() + 4

        val firstTextRect6 = Rect(
            imageRect6.left + 4,
            firstTextTop6,
            scb6.first - 4,
            scb6.second
        )

        page.canvas.drawText(
            firstLineText6,
            firstTextRect6.left.toFloat(),
            firstTextRect6.top.toFloat(),
            textPaint
        )

        val secondTextTop6 = firstTextTop6 + 8 + secondLineBounds6.height()

        val secondTextRect6 = Rect(
            imageRect6.left + 4,
            secondTextTop6,
            scb6.first - 4,
            scb6.second
        )

        page.canvas.drawText(
            secondLineText6,
            secondTextRect6.left.toFloat(),
            secondTextRect6.top.toFloat(),
            textPaint
        )

        return page
    }

    private fun scale(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Pair<Int, Int> {
        // Determine the constrained dimension, which determines both dimensions.
        val width: Int
        val height: Int
        val widthRatio = bitmap.width.toFloat() / maxWidth
        val heightRatio = bitmap.height.toFloat() / maxHeight
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = maxWidth
            height = (width.toFloat() / bitmap.width * bitmap.height).toInt()
        } else {
            height = maxHeight
            width = (height.toFloat() / bitmap.height * bitmap.width).toInt()
        }
        return Pair(width, height)
    }
}