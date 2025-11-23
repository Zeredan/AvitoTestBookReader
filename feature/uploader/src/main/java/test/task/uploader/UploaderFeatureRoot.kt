package test.task.uploader

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import test.task.ui.composables.NavigationMenu
import test.task.ui.themes.AvitoThemeManager


@Composable
fun UploaderFeatureRoot(
    modifier: Modifier = Modifier,
    navigateToBooks: () -> Unit,
    navigateToProfile: () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .weight(1f)
                .background(colorResource(colorScheme.bgPrimary))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
        NavigationMenu(
            activeItem = 1,
            onSelect = {
                when(it) {
                    0 -> navigateToBooks()
                    2 -> navigateToProfile()
                }
            }
        )
    }
}