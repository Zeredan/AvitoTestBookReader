package test.task.uploader

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import test.task.ui.R
import test.task.ui.composables.NavigationMenu
import test.task.ui.themes.AvitoThemeManager


@Composable
fun UploaderFeatureRoot(
    modifier: Modifier = Modifier,
    vm: UploaderViewModel = hiltViewModel(),
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
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.upload_title),
                    fontSize = 22.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W400,
                )
            }

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