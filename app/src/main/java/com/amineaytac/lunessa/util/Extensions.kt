package com.amineaytac.lunessa.util

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle?) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction.actionId, bundle)
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}