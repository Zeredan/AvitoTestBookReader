package test.task.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import test.task.ui.R
import test.task.ui.themes.AvitoThemeManager

@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    isDownloaded: Boolean,
    title: String,
    author: String,
    progress: Float,
    imageUrl: String?,
    @DrawableRes imagePlaceholder: Int,
    onCardClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(colorScheme.bookCardBg))
            .clickable { onCardClick() }
    ) {
        imageUrl?.let {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = it,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        } ?: Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(imagePlaceholder),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Text(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(8.dp),
            text = title,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colorResource(colorScheme.bookTitleText),
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W500,
        )
        Text(
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(8.dp),
            text = author,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colorResource(colorScheme.bookAuthorText),
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W500,
        )
        val progressPercent = (progress * 100).toInt()
        Text(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(8.dp),
            text = "$progressPercent%",
            fontSize = 16.sp,
            lineHeight = 18.sp,
            color = colorResource(colorScheme.textPrimary),
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W500,
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(30.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(colorResource(if (isDownloaded) colorScheme.deleteBg else colorScheme.downloadBg))
                .clickable {
                    if (isDownloaded) {
                        onDeleteClick()
                    } else {
                        onDownloadClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isDownloaded) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(iconScheme.iconDelete),
                    contentDescription = null
                )
            } else {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(iconScheme.iconDownload),
                    contentDescription = null
                )
            }
        }
    }
}