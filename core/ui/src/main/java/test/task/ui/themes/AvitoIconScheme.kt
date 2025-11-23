package test.task.ui.themes

import androidx.annotation.DrawableRes
import test.task.ui.R

enum class AvitoIconScheme(
    @DrawableRes val iconBackArrow: Int,
    @DrawableRes val iconHouseActive: Int,
    @DrawableRes val iconHouseInactive: Int,
    @DrawableRes val iconOdnokl: Int,
    @DrawableRes val iconProfileActive: Int,
    @DrawableRes val iconProfileInactive: Int,
    @DrawableRes val iconUploadActive: Int,
    @DrawableRes val iconUploadInactive: Int,
    @DrawableRes val iconSearch: Int,
    @DrawableRes val iconVk: Int,
    @DrawableRes val iconGoogle: Int,
    @DrawableRes val iconLogo: Int,
    @DrawableRes val iconDelete: Int,
    @DrawableRes val iconDownload: Int,
    @DrawableRes val iconProfilePlaceholder: Int,
) {
    DARK(
        iconBackArrow = R.drawable.dark_back_arrow,
        iconHouseActive = R.drawable.dark_home_active,
        iconHouseInactive = R.drawable.dark_home_inactive,
        iconOdnokl = R.drawable.dark_odnokl,
        iconProfileActive = R.drawable.dark_account_active,
        iconProfileInactive = R.drawable.dark_account_inactive,
        iconSearch = R.drawable.dark_search,
        iconVk = R.drawable.dark_vk,
        iconGoogle = R.drawable.dark_google,
        iconLogo = R.drawable.books_logo,
        iconUploadActive = R.drawable.dark_upload_active,
        iconUploadInactive = R.drawable.dark_upload,
        iconDelete = R.drawable.dark_delete,
        iconDownload = R.drawable.dark_download,
        iconProfilePlaceholder = R.drawable.dark_account_active
    ),
    LIGHT(
        iconBackArrow = R.drawable.light_back_arrow,
        iconHouseActive = R.drawable.dark_home_active,
        iconHouseInactive = R.drawable.light_home_inactive,
        iconOdnokl = R.drawable.dark_odnokl,
        iconProfileActive = R.drawable.dark_account_active,
        iconProfileInactive = R.drawable.light_account_inactive,
        iconSearch = R.drawable.light_search,
        iconVk = R.drawable.dark_vk,
        iconGoogle = R.drawable.dark_google,
        iconLogo = R.drawable.books_logo,
        iconUploadActive = R.drawable.dark_upload_active,
        iconUploadInactive = R.drawable.light_upload,
        iconDelete = R.drawable.light_delete,
        iconDownload = R.drawable.light_download,
        iconProfilePlaceholder = R.drawable.dark_account_active
    )
}