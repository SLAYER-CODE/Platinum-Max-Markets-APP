package com.example.finalproductos

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException

object ExifUtil {
    /**
     * @see http://sylvana.net/jpegcrop/exif_orientation.html
     */
    fun rotateBitmap(src: String, bitmap: Bitmap): Bitmap {
        try {
            val orientation = getExifOrientation(src)
            if (orientation == 1) {
                return bitmap
            }
            val matrix = Matrix()
            when (orientation) {
                2 -> matrix.setScale(-1f, 1f)
                3 -> matrix.setRotate(180f)
                4 -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                5 -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                6 -> matrix.setRotate(90f)
                7 -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                8 -> matrix.setRotate(-90f)
                else -> return bitmap
            }
            return try {
                val oriented =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun rotateYUV420Degree90(
        data: ByteArray,
        imageWidth: Int,
        imageHeight: Int
    ): ByteArray? {
        val yuv = ByteArray(imageWidth * imageHeight )
        // Rotate the Y luma
        var i = 0
        for (x in 0 until imageWidth) {
            for (y in imageHeight - 1 downTo 0) {
                yuv[i] = data[y * imageWidth + x]
                i++
            }
        }

        // Rotate the U and V color components
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
        return yuv
    }


    @Throws(IOException::class)
    private fun getExifOrientation(src: String): Int {
        var orientation = 1
        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                val exifClass = Class.forName("android.media.ExifInterface")
                val exifConstructor = exifClass.getConstructor(
                    *arrayOf<Class<*>>(
                        String::class.java
                    )
                )
                val exifInstance = exifConstructor.newInstance(*arrayOf<Any>(src))
                val getAttributeInt = exifClass.getMethod(
                    "getAttributeInt", *arrayOf<Class<*>?>(
                        String::class.java,
                        Int::class.javaPrimitiveType
                    )
                )
                val tagOrientationField = exifClass.getField("TAG_ORIENTATION")
                val tagOrientation = tagOrientationField[null] as String
                orientation =
                    getAttributeInt.invoke(exifInstance, *arrayOf<Any>(tagOrientation, 1)) as Int
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return orientation
    }
}
