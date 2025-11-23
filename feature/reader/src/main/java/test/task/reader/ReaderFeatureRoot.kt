package test.task.reader

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import test.task.settings.AvitoTheme
import test.task.ui.R
import test.task.ui.themes.AvitoThemeManager

@Composable
fun ReaderFeatureRoot(
    modifier: Modifier = Modifier,
    vm: ReaderViewModel,
    bookId: String,
    onBack: () -> Unit,
) {
    val context = LocalContext.current as ComponentActivity
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()

    val selectedBook by vm.selectedBook.collectAsState()
    val chunks by vm.chunks.collectAsState()
    val appTheme by vm.appTheme.collectAsState()
    val fontSize by vm.fontSize.collectAsState()
    val rowInterval by vm.rowInterval.collectAsState()

    var expandedMenuView by remember{ mutableStateOf(false)}

    val bgColor = colorResource(colorScheme.bgPrimary).toArgb()

    LaunchedEffect(bgColor) {
        val window = context.window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = bgColor
    }
    LaunchedEffect(bookId) {
        vm.tryToInitialize(bookId)
    }

    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(colorScheme.bgPrimary))
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(colorResource(colorScheme.backArrowBg))
                    .clickable { onBack() }
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(iconScheme.iconBackArrow),
                    contentDescription = null
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = selectedBook?.title ?: "???",
                fontSize = 20.sp,
                color = colorResource(colorScheme.textPrimary),
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.W400,
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(colorResource(colorScheme.AABg))
                    .clickable {
                        expandedMenuView = !expandedMenuView
                    }
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "AA",
                    color = colorResource(colorScheme.textPrimary),
                    fontSize = 16.sp,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W400,
                )
            }
        }
        AnimatedVisibility(visible = expandedMenuView) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .background(colorResource(colorScheme.settingsBg))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var appThemeExpanded by remember { mutableStateOf(false) }
                    Text(
                        text = stringResource(R.string.app_theme) + ": ",
                        color = colorResource(colorScheme.textPrimary),
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W400,
                    )
                    Spacer(Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorResource(colorScheme.themeBg))
                                .clickable {
                                    appThemeExpanded = !appThemeExpanded
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = appTheme.name,
                                color = colorResource(colorScheme.textPrimary),
                                fontSize = 16.sp,
                                fontFamily = robotoFontFamily,
                                fontWeight = FontWeight.W400,
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width(200.dp)
                                .background(colorResource(colorScheme.themeBg)),
                            expanded = appThemeExpanded,
                            onDismissRequest = {
                                appThemeExpanded = false
                            }
                        ) {
                            AvitoTheme.entries.forEach { theme ->
                                DropdownMenuItem(
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(colorResource(colorScheme.themeBg))
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = theme.name,
                                                color = colorResource(colorScheme.textPrimary),
                                                fontSize = 16.sp,
                                                fontFamily = robotoFontFamily,
                                                fontWeight = FontWeight.W400,
                                            )
                                        }
                                    },
                                    onClick = {
                                        vm.setAppTheme(theme)
                                        appThemeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var fontSizeExpanded by remember { mutableStateOf(false) }
                    Text(
                        text = stringResource(R.string.font_size) + ": ",
                        color = colorResource(colorScheme.textPrimary),
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W400,
                    )
                    Spacer(Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorResource(colorScheme.themeBg))
                                .clickable {
                                    fontSizeExpanded = !fontSizeExpanded
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fontSize.toString(),
                                color = colorResource(colorScheme.textPrimary),
                                fontSize = 16.sp,
                                fontFamily = robotoFontFamily,
                                fontWeight = FontWeight.W400,
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width(200.dp)
                                .background(colorResource(colorScheme.themeBg)),
                            expanded = fontSizeExpanded,
                            onDismissRequest = {
                                fontSizeExpanded = false
                            }
                        ) {
                            listOf(14f, 16f, 18f, 20f).forEach { fontSizeItem ->
                                DropdownMenuItem(
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(colorResource(colorScheme.themeBg))
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = fontSizeItem.toString(),
                                                color = colorResource(colorScheme.textPrimary),
                                                fontSize = 16.sp,
                                                fontFamily = robotoFontFamily,
                                                fontWeight = FontWeight.W400,
                                            )
                                        }
                                    },
                                    onClick = {
                                        vm.setFontSize(fontSizeItem)
                                        fontSizeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var rowIntervalExpanded by remember { mutableStateOf(false) }
                    Text(
                        text = stringResource(R.string.lines_interval) + ": ",
                        color = colorResource(colorScheme.textPrimary),
                        fontSize = 16.sp,
                        fontFamily = robotoFontFamily,
                        fontWeight = FontWeight.W400,
                    )
                    Spacer(Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(colorResource(colorScheme.themeBg))
                                .clickable {
                                    rowIntervalExpanded = !rowIntervalExpanded
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = rowInterval.toString(),
                                color = colorResource(colorScheme.textPrimary),
                                fontSize = 16.sp,
                                fontFamily = robotoFontFamily,
                                fontWeight = FontWeight.W400,
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .width(200.dp)
                                .background(colorResource(colorScheme.themeBg)),
                            expanded = rowIntervalExpanded,
                            onDismissRequest = {
                                rowIntervalExpanded = false
                            }
                        ) {
                            listOf(12f, 16f, 20f, 24f).forEach { rowIntervalItem ->
                                DropdownMenuItem(
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(colorResource(colorScheme.themeBg))
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = rowIntervalItem.toString(),
                                                color = colorResource(colorScheme.textPrimary),
                                                fontSize = 16.sp,
                                                fontFamily = robotoFontFamily,
                                                fontWeight = FontWeight.W400,
                                            )
                                        }
                                    },
                                    onClick = {
                                        vm.setRowInterval(rowIntervalItem)
                                        rowIntervalExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(
                items = chunks,
                key = { ind, chunk -> chunk.hashCode() }
            ) { ind, chunk ->
                LaunchedEffect(1) {
                    vm.updateProgressUpper((ind + 1).toFloat() / chunks.size)
                }
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = chunk,
                    color = colorResource(colorScheme.textPrimary),
                    fontSize = fontSize.sp,
                    lineHeight = rowInterval.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            progress = {selectedBook?.readProgress ?: 0f},
            color = colorResource(colorScheme.progressBarUnfilled),
            trackColor = colorResource(colorScheme.progressBarFilled),
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
