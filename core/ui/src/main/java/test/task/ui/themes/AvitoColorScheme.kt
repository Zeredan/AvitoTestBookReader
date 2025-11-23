package test.task.ui.themes

import androidx.annotation.ColorRes
import test.task.ui.R

enum class AvitoColorScheme(
    //Общие цвета
    @ColorRes val bgPrimary: Int,
    @ColorRes val textPrimary: Int,
    @ColorRes val textSecondary: Int,
    @ColorRes val textError: Int,
    @ColorRes val topBarBg: Int,
    @ColorRes val navDivider: Int,
    @ColorRes val navigationBg: Int,
    @ColorRes val navigationSelectedBg: Int,
    @ColorRes val navigationSelectedText: Int,
    @ColorRes val textFieldBg: Int,
    @ColorRes val loadingIndicator: Int,
    @ColorRes val progressBarFilled: Int,
    @ColorRes val progressBarUnfilled: Int,

    //Цвета SPLASH
    @ColorRes val splashLogoBg: Int,
    //Цвета AUTH
    @ColorRes val registerBg: Int,
    @ColorRes val logInBgActive: Int,
    @ColorRes val logInBgInactive: Int,
    @ColorRes val logInVkBg: Int,
    @ColorRes val logInGoogleBg: Int,
    @ColorRes val logInOkBg1_lb: Int,
    @ColorRes val logInOkBg2_rt: Int,
    //Цвета UPLOADER
    @ColorRes val fileChooserBg: Int,
    @ColorRes val uploadBg: Int,
    //Цвета BOOKS
    @ColorRes val booksListBg: Int,
    @ColorRes val bookCardBg: Int,
    @ColorRes val deleteBg: Int,
    @ColorRes val downloadBg: Int,
    @ColorRes val bookTitleText: Int,
    @ColorRes val bookAuthorText: Int,
    //Цвета READER
    @ColorRes val AABg: Int,
    @ColorRes val pageBg: Int,
    @ColorRes val settingsBg: Int,
    @ColorRes val themeMenuBg: Int,
    //Цвета PROFILE
    @ColorRes val photoBg: Int,
    @ColorRes val logoutBg: Int,
    @ColorRes val editBg: Int,
    @ColorRes val saveChangesBg: Int,
) {
    LIGHT(
        bgPrimary = R.color.light_bg_primary,
        textPrimary = R.color.light_text_primary,
        textSecondary = R.color.light_text_secondary,
        textError = R.color.light_text_error,
        topBarBg = R.color.light_topbar_bg,
        navDivider = R.color.light_nav_divider,
        navigationBg = R.color.light_navigation_bg,
        navigationSelectedBg = R.color.light_navigation_bg_selected,
        navigationSelectedText = R.color.light_navigation_bg_selected_text,
        textFieldBg = R.color.light_textfield_bg,
        loadingIndicator = R.color.light_loading_indicator,
        progressBarFilled = R.color.light_progress_filled,
        progressBarUnfilled = R.color.light_progress_unfilled,

        splashLogoBg = R.color.light_splash_logo_bg,

        registerBg = R.color.light_register_bg,
        logInBgActive = R.color.light_login_active_bg,
        logInBgInactive = R.color.light_login_inactive_bg,
        logInVkBg = R.color.light_login_vk_bg,
        logInGoogleBg = R.color.light_login_google_bg,
        logInOkBg1_lb = R.color.light_login_ok_bg1_lb,
        logInOkBg2_rt = R.color.light_login_ok_bg2_rt,

        fileChooserBg = R.color.light_filechooser_bg,
        uploadBg = R.color.light_upload_bg,

        booksListBg = R.color.light_books_list_bg,
        bookCardBg = R.color.light_book_card_bg,
        deleteBg = R.color.light_delete_bg,
        downloadBg = R.color.light_download_bg,
        bookTitleText = R.color.light_book_title,
        bookAuthorText = R.color.light_book_author,

        AABg = R.color.light_reader_aa_bg,
        pageBg = R.color.light_page_bg,
        settingsBg = R.color.light_settings_bg,
        themeMenuBg = R.color.light_theme_menu_bg,

        photoBg = R.color.light_photo_bg,
        logoutBg = R.color.light_logout_bg,
        editBg = R.color.light_edit_bg,
        saveChangesBg = R.color.light_save_changes_bg
    ),

    DARK(
        bgPrimary = R.color.dark_bg_primary,
        textPrimary = R.color.dark_text_primary,
        textSecondary = R.color.dark_text_secondary,
        textError = R.color.dark_text_error,
        topBarBg = R.color.dark_topbar_bg,
        navDivider = R.color.dark_nav_divider,
        navigationBg = R.color.dark_navigation_bg,
        navigationSelectedBg = R.color.dark_navigation_bg_selected,
        navigationSelectedText = R.color.dark_navigation_bg_selected_text,
        textFieldBg = R.color.dark_textfield_bg,
        loadingIndicator = R.color.dark_loading_indicator,
        progressBarFilled = R.color.dark_progress_filled,
        progressBarUnfilled = R.color.dark_progress_unfilled,

        splashLogoBg = R.color.dark_splash_logo_bg,

        registerBg = R.color.dark_register_bg,
        logInBgActive = R.color.dark_login_active_bg,
        logInBgInactive = R.color.dark_login_inactive_bg,
        logInVkBg = R.color.dark_login_vk_bg,
        logInGoogleBg = R.color.dark_login_google_bg,
        logInOkBg1_lb = R.color.dark_login_ok_bg1_lb,
        logInOkBg2_rt = R.color.dark_login_ok_bg2_rt,

        fileChooserBg = R.color.dark_filechooser_bg,
        uploadBg = R.color.dark_upload_bg,

        booksListBg = R.color.dark_books_list_bg,
        bookCardBg = R.color.dark_book_card_bg,
        deleteBg = R.color.dark_delete_bg,
        downloadBg = R.color.dark_download_bg,
        bookTitleText = R.color.dark_book_title,
        bookAuthorText = R.color.dark_book_author,

        AABg = R.color.dark_reader_aa_bg,
        pageBg = R.color.dark_page_bg,
        settingsBg = R.color.dark_settings_bg,
        themeMenuBg = R.color.dark_theme_menu_bg,

        photoBg = R.color.dark_photo_bg,
        logoutBg = R.color.dark_logout_bg,
        editBg = R.color.dark_edit_bg,
        saveChangesBg = R.color.dark_save_changes_bg
    );
}