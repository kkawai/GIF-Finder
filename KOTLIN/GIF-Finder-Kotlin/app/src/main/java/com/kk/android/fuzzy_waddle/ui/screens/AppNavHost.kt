package com.kk.android.fuzzy_waddle.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavHost() {

    val gifFinderViewModel: GifFinderViewModel =
        viewModel(factory = GifFinderViewModel.Factory)

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {

        composable(
            Screen.HomeScreen.route
        ) {
            HomeScreen(
                onOverflowMenuClicked = {
                    navController.navigate(Screen.PrivacyPolicyScreen.route)
                },
                onGifImageClicked = { gifImageUrl, gifImageAspectRatio ->
                    navController.navigate(Screen.DetailedGIFScreen.route + "/${gifImageUrl}/${gifImageAspectRatio}")
                },
                gifFinderViewModel = gifFinderViewModel,
                retryAction = { gifFinderViewModel.getGifImages() })
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