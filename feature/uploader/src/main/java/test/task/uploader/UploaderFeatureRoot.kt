package test.task.uploader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
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

    val errorMessage by vm.errorMessage.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    val isValid by vm.isValid.collectAsState()
    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()
    val fileUri by vm.fileUri.collectAsState()
    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let { vm.setFileUri(it) }
        }
    }

    LaunchedEffect(1) {
        vm.successEventFlow.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(colorResource(colorScheme.fileChooserBg))
                    .clickable {
                        val pickFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "*/*"
                            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                                "text/plain",
                                "application/pdf",
                                "application/epub+zip"
                            ))
                        }
                        pickFileLauncher.launch(pickFileIntent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.chose_file),
                    fontSize = 14.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W600,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(16.dp))
            fileUri?.toString()?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(colorResource(colorScheme.photoBg))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uri,
                        fontSize = 14.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                onValueChange = vm::setTitle,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(colorScheme.textFieldBg),
                    unfocusedContainerColor = colorResource(colorScheme.textFieldBg),
                    focusedTextColor = colorResource(colorScheme.textPrimary),
                    unfocusedTextColor = colorResource(colorScheme.textPrimary),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.book_title_placeholder),
                        fontSize = 18.sp,
                        color = colorResource(colorScheme.textSecondary),
                    )
                },
                shape = RoundedCornerShape(5.dp)
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = author,
                onValueChange = vm::setAuthor,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(colorScheme.textFieldBg),
                    unfocusedContainerColor = colorResource(colorScheme.textFieldBg),
                    focusedTextColor = colorResource(colorScheme.textPrimary),
                    unfocusedTextColor = colorResource(colorScheme.textPrimary),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.author_placeholder),
                        fontSize = 18.sp,
                        color = colorResource(colorScheme.textSecondary),
                    )
                },
                shape = RoundedCornerShape(5.dp)
            )
            Spacer(Modifier.height(24.dp))
            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = colorResource(colorScheme.textError),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(if (isValid) colorScheme.uploadBgActive else colorScheme.uploadBgInactive))
                    .run{
                        if (isValid) clickable {
                            vm.uploadBook()
                        } else this
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = colorResource(colorScheme.loadingIndicator),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                } else {
                    Text(
                        text = stringResource(if (errorMessage == null) R.string.upload else R.string.upload_repeat),
                        fontSize = 18.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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