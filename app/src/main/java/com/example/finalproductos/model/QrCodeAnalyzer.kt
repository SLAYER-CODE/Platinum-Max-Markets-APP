package com.example.finalproductos.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat.*
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.finalproductos.ExifUtil.rotateYUV420Degree90
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}


class QrCodeAnalyzer(
//    private val onEan: (result:Result)->Unit):ImageAnalysis.Analyzer{
//    }

    private val  onQrCodesDetected: (qrCode: Result) -> Unit
) : ImageAnalysis.Analyzer
{
    private val yuvFormats = mutableListOf(YUV_420_888)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            yuvFormats.addAll(listOf(YUV_422_888, YUV_444_888,YUV_420_888))
        }
    }
    companion object{
        val reader = MultiFormatReader()

    }

//    private val reader = MultiFormatReader().apply {
//        val map = mapOf(
//            DecodeHintType.ALLOWED_EAN_EXTENSIONS to arrayListOf(BarcodeFormat.EAN_13),DecodeHintType.POSSIBLE_FORMATS to arrayListOf()
//        )
//        reset()
//        setHints(map)
//    }
//    private val reader= MultiFormatReader().apply {
//        val map = mapOf(
//            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.EAN_13,BarcodeFormat.QR_CODE))
//        reset()
//        setHints(map)
//    }

        //    fun setFormats(formats: List<BarcodeFormat>) = barcodeReader.setHints(
//        mapOf(
//            DecodeHintType.POSSIBLE_FORMATS to formats
//        )
//    )
//open fun rotateYUV420Degree90(
//    data: ByteArray,
//    imageWidth: Int,
//    imageHeight: Int
//): ByteArray? {
//    val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
//    // Rotate the Y luma
//    var i = 0
//    for (x in 0 until imageWidth) {
//        for (y in imageHeight - 1 downTo 0) {
//            yuv[i] = data[y * imageWidth + x]
//            i++
//        }
//    }
//    // Rotate the U and V color components
//    i = imageWidth * imageHeight * 3 / 2 - 1
//    var x = imageWidth - 1
//    while (x > 0) {
//        for (y in 0 until imageHeight / 2) {
//            yuv[i] = data[imageWidth * imageHeight + y * imageWidth + x]
//            i--
//            yuv[i] = data[imageWidth * imageHeight + y * imageWidth + (x - 1)]
//            i--
//        }
//        x = x - 2
//    }
//    return yuv
//}


    override fun analyze(image: ImageProxy) {
        // We are using YUV format because, ImageProxy internally uses ImageReader to get the image
        // by default ImageReader uses YUV format unless changed.

        if (image.format !in yuvFormats) {
            Log.e("QRCodeAnalyzer", "Expected YUV, now = ${image.format}")
            image.close()
            return
        }

//        println(image.format)
//        println(image.width)
//        println(image.height)
//        println(image.planes.size)

//        println(image.planes[0].buffer.toString())
//        println(image.planes.toString())
//        println(image.planes[0].toString())
//        println(image.planes[0].buffer.toByteArray().size)
//        println(image.planes[0].buffer.toByteArray().toString())
//        val rotatedData =  image.planes[0].buffer.toByteArray()
//        for (y in 0 until image.planes[0].buffer.toByteArray().size) {
//            for (x in 0 until  image.planes[0].buffer.toByteArray().leng) rotatedData[x *  image.planes[0].buffer.toByteArray().size +  image.planes[0].buffer.toByteArray().size - y - 1] =
//                image.planes[0].buffer.toByteArray().get(x + y *  image.planes[0].buffer.toByteArray().size)
//        }

//        val data = rotateYUV420Degree90(image.planes[0].buffer.toByteArray(),image.planes[0].buffer.toByteArray().size,image.planes[0].buffer.toByteArray().size)
//        val data =  image.planes[0].buffer.toByteArray()
        val data = rotateYUV420Degree90(image.planes[0].buffer.toByteArray(),image.width,image.height)
//        val data = rotatedData
//        val myImg =BitmapFactory.decodeByteArray(data,0,data.size)
//    val matrix = Matrix()
//    matrix.postRotate(30f)
//
//    val rotated = Bitmap.createBitmap(
//        myImg, 0, 0, myImg.width, myImg.height,
//        matrix, true
//    )
//
//
//    val stream = ByteArrayOutputStream()
//    rotated.compress(Bitmap.CompressFormat.PNG, 90, stream)
//
//    val finalfinalfinal = stream.toByteArray()

//        val final=rotateYUV420Degree90(image.planes[0].buffer.toByteArray(),image.height,image.height)
//        val bytes=ByteArray(data.capacity())
//        data.get(bytes)
//        val rotatedImage = RotatedImage(bytes, image.width, image.height)
//        rotateImageArray(rotatedImage,image.imageInfo.rotationDegrees)
//        Log.i("MIOO",image.imageInfo.rotationDegrees.toString())
//        toByteArray().reversedArray()
//        val rotatedData = image.planes[0].buffer.toByteArray()
//        for (y in 0 until image.height) {
//            for (x in 0 until image.width){
//                rotatedData.get((x * image.height + image.height - y - 1)) =
//                data[x + y * image.width]
//            }
//        }
    val source = PlanarYUVLuminanceSource(
            data,
            image.height,
            image.width,
        200,
        200,
            image.height-200,
            image.width-200,
            false
    )
//        ).crop(0,0,image.height-200,image.width-200)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
//        val resPoint:ResultPoint= ResultPoint(202f,201f)
        try {
            // Whenever reader fails to detect a QR code in image
            // it throws NotFoundException
            val result = reader.decode(binaryBitmap)
//            val result = reader.decode(binaryBitmap)
//            result.addResultPoints(arrayOf(resPoint))

            onQrCodesDetected(result)

        } catch (e: NotFoundException ) {
            e.printStackTrace()
        }
        image.close()
    }

//
//    private fun rotateImageArray(imageToRotate: RotatedImage, rotationDegrees: Int) {
//        if (rotationDegrees == 0) return // no rotation
//        if (rotationDegrees % 90 != 0) return // only 90 degree times rotations
//
//        val width = imageToRotate.width
//        val height = imageToRotate.height
//
//        val rotatedData = ByteArray(imageToRotate.byteArray.size)
//        for (y in 0 until height) { // we scan the array by rows
//            for (x in 0 until width) {
//                when (rotationDegrees) {
//                    90 -> rotatedData[x * height + height - y - 1] =
//                        imageToRotate.byteArray[x + y * width] // Fill from top-right toward left (CW)
//                    180 -> rotatedData[width * (height - y - 1) + width - x - 1] =
//                        imageToRotate.byteArray[x + y * width] // Fill from bottom-right toward up (CW)
//                    270 -> rotatedData[y + x * height] =
//                        imageToRotate.byteArray[y * width + width - x - 1] // The opposite (CCW) of 90 degrees
//                }
//            }
//        }
//
//        imageToRotate.byteArray = rotatedData
//
//        if (rotationDegrees != 180) {
//            imageToRotate.height = width
//            imageToRotate.width = height
//        }
//    }
//    private data class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int)

}