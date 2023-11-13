package com.kk.android.kt.gf

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kk.android.kt.gf.ui.screens.GifFinderViewModel
import com.kk.android.kt.gf.ui.screens.NavigationState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rules.TestDispatcherRule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)  //uncomment if using context
class GifFinderViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.kk.android.kt.gf", appContext.packageName)
//    }

    @Test
    fun testViewModel() {
        runTest {
            val viewModel = GifFinderViewModel(gifImageRepository = FakeGifImageRepository())
            assertEquals(NavigationState.HomeScreen, viewModel.navigationState)
            assertEquals(viewModel.screenState.value.imageCount, 0)
            assertEquals(true, viewModel.screenState.value.isEndReached)
            assertEquals(viewModel.screenState.value.gifImages.size, 0)
            assertEquals(viewModel.screenState.value.error, "")
        }
    }

}