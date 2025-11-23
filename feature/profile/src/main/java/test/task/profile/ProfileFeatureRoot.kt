package test.task.profile

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import test.task.auth.AuthState
import test.task.ui.composables.NavigationMenu
import test.task.ui.themes.AvitoThemeManager
import test.task.ui.R


@Composable
fun ProfileFeatureRoot(
    modifier: Modifier = Modifier,
    vm: ProfileViewModel,
    navigateToBooks: () -> Unit,
    navigateToUploader: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()

    val authState by vm.authState.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val isEditing by vm.isEditing.collectAsState()

    val nickname by vm.nickName.collectAsState()
    val phone by vm.phoneNumber.collectAsState()
    val photoUri by vm.imageUri.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .weight(1f)
                .background(colorResource(colorScheme.bgPrimary))
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
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
                    text = stringResource(R.string.profile_title),
                    fontSize = 22.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W400,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(colorResource(colorScheme.photoBg)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(colorScheme.photoBg)),
                    painter = painterResource(iconScheme.iconProfilePlaceholder),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                if (authState is AuthState.Success && (authState as? AuthState.Success)?.user?.photoUrl != null) {
                    println("EEEE P ${(authState as AuthState.Success).user.photoUrl}")
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(colorScheme.photoBg)),
                        model = (authState as? AuthState.Success)?.user?.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (authState as? AuthState.Success)?.user?.displayName ?: "Guest",
                fontSize = 22.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W500,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.email_profile),
                    fontSize = 16.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W500,
                )
                Text(
                    text = (authState as? AuthState.Success)?.user?.email ?: "???",
                    fontSize = 16.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.phone_number),
                    fontSize = 16.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W500,
                )
                Text(
                    text = (authState as? AuthState.Success)?.user?.phoneNumber?.takeIf { it.isNotEmpty() } ?: "???",
                    fontSize = 16.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.weight(1f))
            if (!isEditing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(colorScheme.editBg))
                        .clickable {
                            vm.startEditing()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorResource(colorScheme.loadingIndicator),
                            strokeWidth = 4.dp,
                            strokeCap = StrokeCap.Round
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.edit),
                            fontSize = 18.sp,
                            color = colorResource(colorScheme.textPrimary),
                            fontFamily = robotoFontFamily,
                            fontWeight = FontWeight.W600,
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(colorScheme.saveChangesBg))
                        .clickable {
                            vm.stopEditing()
                            vm.saveChanges()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.save_changes),
                        fontSize = 18.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W600,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            if (isEditing) {
                val pickImageLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    uri?.let { vm.setPhoto(it) }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(colorResource(colorScheme.photoBg))
                        .clickable {
                            pickImageLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.photo_upload),
                        fontSize = 14.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(16.dp))
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
                        text = photoUri?.toString() ?: "...",
                        fontSize = 14.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(16.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = nickname,
                    onValueChange = vm::setNickName,
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
                            text = stringResource(R.string.nickname),
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
                    value = phone,
                    onValueChange = vm::setPhoneNumber,
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
                            text = stringResource(R.string.phone_number),
                            fontSize = 18.sp,
                            color = colorResource(colorScheme.textSecondary),
                        )
                    },
                    shape = RoundedCornerShape(5.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(colorScheme.logoutBg))
                    .clickable {
                        vm.logout()
                        onLogout()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    fontSize = 18.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W600,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
        NavigationMenu(
            activeItem = 2,
            onSelect = {
                when(it) {
                    0 -> navigateToBooks()
                    1 -> navigateToUploader()
                }
            }
        )
    }
}