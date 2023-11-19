package com.kk.android.fuzzy_waddle

import androidx.lifecycle.SavedStateHandle
import com.kk.android.fuzzy_waddle.ui.screens.GifFinderViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
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
//        assertEquals("com.kk.android.fuzzy_waddle", appContext.packageName)
//    }

    @Test
    fun testViewModel() {
        runTest {
            val viewModel =
                GifFinderViewModel(gifImageRepository = FakeGifImageRepository())
            assertEquals(viewModel.homeScreenState.value.imageCount, 0)
            assertEquals(true, viewModel.homeScreenState.value.isEndReached)
            assertEquals(viewModel.homeScreenState.value.gifImages.size, 0)
            assertEquals(viewModel.homeScreenState.value.error, "")
        }
    }

}