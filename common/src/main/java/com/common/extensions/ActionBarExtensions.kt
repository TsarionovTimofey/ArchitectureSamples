package com.common.extensions

import androidx.appcompat.app.ActionBar

/**
 * Created on 05.08.2020.
 */
fun ActionBar.initHomeButtonAndTitle(
    showHomeButton: Boolean = true,
    homeAsUpEnabled: Boolean = true,
    showTitle: Boolean = false
) {
    setDisplayShowHomeEnabled(showHomeButton)
    setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    setDisplayShowTitleEnabled(showTitle)
}