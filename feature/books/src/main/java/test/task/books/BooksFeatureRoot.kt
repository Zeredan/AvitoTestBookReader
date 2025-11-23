package test.task.books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import test.task.ui.R
import test.task.ui.composables.BookCard
import test.task.ui.composables.NavigationMenu
import test.task.ui.composables.SearchTextField
import test.task.ui.themes.AvitoThemeManager


@Composable
fun BooksFeatureRoot(
    modifier: Modifier = Modifier,
    vm: BooksViewModel = hiltViewModel(),
    navigateToUploader: () -> Unit,
    navigateToProfile: () -> Unit,
    onBookClick: (String) -> Unit
) {
    val context = LocalContext.current
    val colorScheme by AvitoThemeManager.colorScheme.collectAsState()
    val iconScheme by AvitoThemeManager.iconScheme.collectAsState()
    val robotoFontFamily = AvitoThemeManager.RobotoFontFamily()

    val books by vm.books.collectAsState()
    val searchText by vm.searchText.collectAsState()
    val loadingProgress by vm.loadingProgress.collectAsState()

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
                    .padding(top = 16.dp)
                    .height(56.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.my_books_title),
                    fontSize = 22.sp,
                    color = colorResource(colorScheme.textPrimary),
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.W400,
                )
                SearchTextField(
                    value = searchText,
                    onValueChange = vm::setSearchText,
                    placeholder = stringResource(R.string.search_books)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            loadingProgress?.let{
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    progress = {it},
                    color = colorResource(colorScheme.progressBarUnfilled),
                    trackColor = colorResource(colorScheme.progressBarFilled),
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(colorResource(colorScheme.booksListBg)),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(books) { book ->
                    BookCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        isDownloaded = book.localPath != null,
                        title = book.title,
                        author = book.author ?: context.getString(R.string.author_unknown),
                        progress = book.readProgress,
                        imageUrl = null,
                        imagePlaceholder = R.drawable.book_placeholder,
                        onCardClick = { onBookClick(book.id) },
                        onDownloadClick = { vm.downloadBook(book) },
                        onDeleteClick = { vm.deleteBook(book, true) }
                    )
                }
            }
        }
        NavigationMenu(
            activeItem = 0,
            onSelect = {
                when(it) {
                    1 -> navigateToUploader()
                    2 -> navigateToProfile()
                }
            }
        )
    }
}