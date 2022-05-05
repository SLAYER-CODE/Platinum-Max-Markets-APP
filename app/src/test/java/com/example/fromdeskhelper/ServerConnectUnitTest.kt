package com.example.fromdeskhelper

import com.example.fromdeskhelper.core.RetrofitHelper
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ServerConnectUnitTest {
    @Test
    fun addition_isCorrect() {
        RetrofitHelper.getRtrofit()
    }

}