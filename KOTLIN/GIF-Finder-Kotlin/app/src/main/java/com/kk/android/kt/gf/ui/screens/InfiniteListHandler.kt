package com.kk.android.kt.gf.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.kk.android.kt.gf.Constants
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun InfiniteListHandler(
    lazyGridState: LazyStaggeredGridState,
    buffer: Int = 2,
    onLoadMore: () -> Unit,
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = lazyGridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            val doMore = totalItems >= Constants.PAGE_SIZE && lastVisibleItemIndex == (totalItems-1)
            //Log.i("InfiniteListHandler ggggg","lastVisibleItemIndex: " + lastVisibleItemIndex
            //        + " totalItems: " + totalItems + " doMore: " + doMore)
            doMore
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                if (loadMore.value == true) {
                    onLoadMore()
                }
            }
    }
}