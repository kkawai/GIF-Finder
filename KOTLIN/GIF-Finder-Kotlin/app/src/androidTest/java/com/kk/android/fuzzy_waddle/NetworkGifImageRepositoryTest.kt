package com.kk.android.fuzzy_waddle

import com.kk.android.fuzzy_waddle.data.NetworkGifImageRepository
import com.kk.android.fuzzy_waddle.model.GiphyImage
import com.kk.android.fuzzy_waddle.model.GiphyResponse
import com.kk.android.fuzzy_waddle.util.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import rules.TestDispatcherRule

class NetworkGifImageRepositoryTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun testNetworkGifImageRepository() = runTest {
        val networkGifImageRepository = NetworkGifImageRepository(
            gifImageApiService = FakeGifImageApiService()
        )
        //there must be 2 'emits'
        assertEquals(2, networkGifImageRepository.getTrendingGifImages(0).count())

        //the first emit must be Loading
        assertEquals(true, networkGifImageRepository.getTrendingGifImages(0).first() is Resource.Loading)

        var i = 0
        val flowCollector = FlowCollector<Resource<GiphyResponse>> {
            if (i==0) {
                assertTrue(it is Resource.Loading)
                println("ttttt here 1")
            } else if (i==1) {
                assertEquals(it.data?.data, emptyList<GiphyImage>())
                assertTrue(it is Resource.Success)
                println("ttttt here 2")
            } else {
                fail("There shouldn't be more than 2 state emissions.  Check networkGifImageRepository.getTrendingGifImages.")
            }
            i++
        }
        println("ttttt here 3")
        networkGifImageRepository.getTrendingGifImages(0).collect(flowCollector)
        delay(3000)
    }
}