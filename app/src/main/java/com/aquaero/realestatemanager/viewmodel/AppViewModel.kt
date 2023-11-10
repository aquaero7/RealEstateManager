package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType

class AppViewModel(
    private val propertyRepository: PropertyRepository
): ViewModel() {
    /*
    private val context: Context
        get() = ApplicationRoot.getContext()
    */
    private val context: Context by lazy { ApplicationRoot.getContext() }

    /**
     * Init content type, according to window's width,
     * to choose dynamically, on screen state changes, whether to show
     * just a list content, or both a list and detail content
     */
    fun contentType(windowSize: WindowWidthSizeClass): AppContentType = when (windowSize) {
        WindowWidthSizeClass.Expanded -> { AppContentType.SCREEN_WITH_DETAIL }
        else -> { AppContentType.SCREEN_ONLY }
    }

    /**
     * TopBar
     */
    fun onClickMenu(currentScreen: String?) = run {
        val msg = when (currentScreen) {
            ListAndDetail.routeWithArgs -> R.string.edit
            Detail.routeWithArgs -> R.string.edit
            else -> R.string.valid
        }
        Toast.makeText(context, "Click on ${context.getString(msg)}", Toast.LENGTH_SHORT).show()
    }
    fun menuIcon(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == SearchCriteria.route ||
        currentScreen == Loan.route
    ) Icons.Default.Check else Icons.Default.Edit
    fun menuIconContentDesc(currentScreen: String?) = if (
        currentScreen == EditDetail.routeWithArgs ||
        currentScreen == Loan.route
    ) R.string.cd_check else R.string.cd_edit
    fun menuEnabled(currentScreen: String?, windowSize: WindowWidthSizeClass) = (
            currentScreen != GeolocMap.route) &&
            (currentScreen != ListAndDetail.routeWithArgs ||
                    contentType(windowSize) == AppContentType.SCREEN_WITH_DETAIL)
    val onClickRadioButton = { currency: String -> Toast
        .makeText(context, "Click on $currency", Toast.LENGTH_SHORT)
        .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun propertyFromId(propertyId: Long): Property {
        return propertyRepository.propertyFromId(propertyId)
    }



}


