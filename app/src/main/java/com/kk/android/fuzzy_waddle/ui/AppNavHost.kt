package com.kk.android.fuzzy_waddle.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kk.android.fuzzy_waddle.ui.common.Screen
import com.kk.android.fuzzy_waddle.ui.details_screen.FullScreenGif
import com.kk.android.fuzzy_waddle.ui.home_screen.GifFinderViewModel
import com.kk.android.fuzzy_waddle.ui.home_screen.HomeScreen
import com.kk.android.fuzzy_waddle.ui.privacy_policy.WebViewScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        composable(
            Screen.HomeScreen.route
        ) {
            val viewModel: GifFinderViewModel =
                viewModel(factory = GifFinderViewModel.Factory)
            HomeScreen(
                onOverflowMenuClicked = {
                    navController.navigate(Screen.PrivacyPolicyScreen.route)
                },
                onGifImageClicked = { gifImageUrl, gifImageAspectRatio ->
                    navController.navigate(Screen.DetailedGIFScreen.route + "/${gifImageUrl}/${gifImageAspectRatio}")
                },
                viewModel = viewModel,
                stateFlow = viewModel.homeScreenState,
                getGifImages = {viewModel.getGifImages()},
                getGifImagesWithSearchTerm = { term -> viewModel.getGifImagesWithSearchTerm(term) },
                lazyStaggeredGridState = viewModel.lazyStaggeredGridState,
                retryAction = { viewModel.getGifImages() })
        }

        composable(
            Screen.DetailedGIFScreen.route + "/{gifImageUrl}/{gifImageAspectRatio}",
            arguments = listOf(
                navArgument("gifImageUrl") {
                    type = NavType.StringType
                }, navArgument("gifImageAspectRatio") {
                    type = NavType.FloatType
                })
        ) {backStackEntry ->
            FullScreenGif(backStackEntry.arguments?.getString("gifImageUrl")?:"",
                backStackEntry.arguments?.getFloat("gifImageAspectRatio")?:1.5f)
        }

        composable(
            Screen.PrivacyPolicyScreen.route
        ) {
            WebViewScreen()
        }
    }
}