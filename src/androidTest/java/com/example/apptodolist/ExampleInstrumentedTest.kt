/**
 * @Author: 
 * @Date: 2024-12-10 22:06:58
 * @LastEditors: 
 * @LastEditTime: 2024-12-10 22:07:32
 * @FilePath: app/src/androidTest/java/com/example/apptodolist/ExampleInstrumentedTest.kt
 * @Description: 这是默认设置,可以在设置》工具》File Description中进行配置
 */
package com.example.apptodolist

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.apptodolist", appContext.packageName)
    }
}