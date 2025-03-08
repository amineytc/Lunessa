package com.amineaytac.lunessa.util

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle?) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction.actionId, bundle)
    }
}