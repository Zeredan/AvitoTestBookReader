package test.task.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import test.task.ui.themes.AvitoThemeManager
import test.task.ui.R
import test.task.ui.composables.GoogleButton
import test.task.ui.composables.LoginInputTextField
import test.task.ui.composables.ODNOKLButton
import test.task.ui.composables.VKButton

@Composable
fun AuthFeatureRoot(
    modifier: Modifier = Modifier,
    vm: AuthViewModel,
    onLoggedIn: () -> Unit
) {
    val context = LocalContext.current
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()

    val authState by vm.authState.collectAsState()
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val isInputValid by vm.isInputsValid.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoggedIn()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(colorScheme.bgPrimary))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.log_in_title),
                fontSize = 22.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W400,
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(36.dp)
                    .background(colorResource(colorScheme.registerBg))
                    .clickable {

                    }
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.registration),
                    fontSize = 16.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W500,
                )
            }
        }
        if (authState !is AuthState.Success) {
            Spacer(Modifier.weight(1f))
            if (authState is AuthState.Error) {
                Text(
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp),
                    text = (authState as AuthState.Error).message,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    color = colorResource(colorScheme.textError),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W400,
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(R.string.email),
                fontSize = 15.sp,
                lineHeight = 18.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W500,
            )
            Spacer(Modifier.height(8.dp))
            LoginInputTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { vm.setEmail(it) },
                placeholder = stringResource(R.string.email_placeholder)
            )
            Spacer(Modifier.height(15.dp))
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(R.string.password),
                fontSize = 15.sp,
                lineHeight = 18.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W500,
            )
            Spacer(Modifier.height(8.dp))
            LoginInputTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { vm.setPassword(it) },
                placeholder = stringResource(R.string.password_placeholder),
                hideSymbol = '*'
            )
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(colorResource(if (isInputValid) colorScheme.logInBgActive else colorScheme.logInBgInactive))
                    .run {
                        if (isInputValid) {
                            clickable {
                                vm.logInOrSignUp()
                            }
                        } else {
                            this
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = colorResource(colorScheme.loadingIndicator),
                        strokeWidth = 3.dp,
                        strokeCap = StrokeCap.Round
                    )
                } else {
                    Text(
                        text = stringResource(R.string.log_in),
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        color = colorResource(colorScheme.textPrimary),
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W500
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.forgot_pwd),
                fontSize = 15.sp,
                lineHeight = 18.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.height(32.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = colorResource(colorScheme.navDivider)
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                GoogleButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        vm.moveToGoogle(context)
                    }
                )
                Spacer(Modifier.width(16.dp))
                VKButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        vm.moveToVK(context)
                    }
                )
                Spacer(Modifier.width(16.dp))
                ODNOKLButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        vm.moveToOdnokl(context)
                    }
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}