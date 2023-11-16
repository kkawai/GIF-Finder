package com.kk.android.fuzzy_waddle

import com.kk.android.fuzzy_waddle.ui.screens.GifFinderViewModel
import com.kk.android.fuzzy_waddle.ui.screens.CurrentScreen
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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
            val viewModel = GifFinderViewModel(gifImageRepository = FakeGifImageRepository())
            assertEquals(CurrentScreen.HomeScreen, viewModel.currentScreen)
            assertEquals(viewModel.screenState.value.imageCount, 0)
            assertEquals(true, viewModel.screenState.value.isEndReached)
            assertEquals(viewModel.screenState.value.gifImages.size, 0)
            assertEquals(viewModel.screenState.value.error, "")
        }
    }

}