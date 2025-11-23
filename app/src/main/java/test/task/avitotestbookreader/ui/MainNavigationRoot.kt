package test.task.avitotestbookreader.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import test.task.auth.AuthFeatureRoot
import test.task.auth.AuthState
import test.task.auth.AuthViewModel
import test.task.books.BooksFeatureRoot
import test.task.profile.ProfileFeatureRoot
import test.task.profile.ProfileViewModel
import test.task.reader.ReaderFeatureRoot
import test.task.reader.ReaderViewModel
import test.task.splash.SplashFeatureRoot
import test.task.ui.themes.AvitoThemeManager
import test.task.uploader.UploaderFeatureRoot
import java.util.Locale

@SuppressLint("NewApi", "ContextCastToActivity")
@Composable
fun MainNavigationRoot(
    modifier: Modifier = Modifier,
    deferredsToWait: List<Deferred<Any>>,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    // Создаю вьюмодели тут, а не внутри фичи - для предотвращения мерцания UI и пре-расчетов
    val authViewModel: AuthViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val readerViewModel: ReaderViewModel = hiltViewModel()
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()

    //val selectedLanguage by settingsViewModel.selectedLanguageStateFlow.collectAsState()
    val activity = LocalContext.current as ComponentActivity

    activity.window.navigationBarColor = colorResource(colorScheme.bgPrimary).toArgb()
    activity.window.statusBarColor = colorResource(colorScheme.bgPrimary).toArgb()
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(colorScheme.bgPrimary)),
        navController = navController,
        startDestination = ScreenState.SPLASH,
        enterTransition = {
            fadeIn(tween(0))
        },
        exitTransition = {
            fadeOut(tween(0))
        }
    ) {
        composable(ScreenState.SPLASH) {
            LaunchedEffect(1) {
                coroutineScope.launch {
                    deferredsToWait.forEach { it.await() }
                    authViewModel.authState.first{ it !is AuthState.Loading }
                    navController.navigate(
                        if (authViewModel.authState.value is AuthState.Success) ScreenState.BOOKS else ScreenState.AUTH
                    ) {
                        popUpTo(ScreenState.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            }
            SplashFeatureRoot(

            )
        }
        composable(ScreenState.AUTH) {
            AuthFeatureRoot(
                vm = authViewModel,
                onLoggedIn = {
                    navController.navigate(ScreenState.BOOKS) {
                        popUpTo(ScreenState.AUTH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(ScreenState.BOOKS) {
            BooksFeatureRoot(
                navigateToUploader = {
                    navController.navigate(ScreenState.UPLOADER) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                },
                navigateToProfile = {
                    navController.navigate(ScreenState.PROFILE) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                },
                onBookClick = { id ->
                    navController.navigate("${ScreenState.READER}/$id") {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(ScreenState.UPLOADER) {
            UploaderFeatureRoot(
                navigateToProfile = {
                    navController.navigate(ScreenState.PROFILE) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                },
                navigateToBooks = {
                    navController.navigate(ScreenState.BOOKS) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(ScreenState.PROFILE) {
            ProfileFeatureRoot(
                vm = profileViewModel,
                navigateToBooks = {
                    navController.navigate(ScreenState.BOOKS) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = true
                        }
                    }
                },
                navigateToUploader = {
                    navController.navigate(ScreenState.UPLOADER) {
                        popUpTo(ScreenState.BOOKS) {
                            inclusive = false
                        }
                    }
                },
                onLogout = {
                    navController.navigate(ScreenState.AUTH) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            "${ScreenState.READER}/{bookId}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("bookId") ?: ""
            ReaderFeatureRoot(
                bookId = id,
                vm = readerViewModel,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

fun applySelectedLanguage(
    activity: ComponentActivity,
    lang: String
) {
    with(activity) {
        println("QFASA: $lang | ${resources.configuration.locales[0]}")
        resources.apply {
            val locale = Locale(lang)
            val config = Configuration(configuration)

            createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            resources.updateConfiguration(config, displayMetrics)
        }
    }
}
