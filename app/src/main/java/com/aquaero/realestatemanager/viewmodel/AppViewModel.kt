package com.aquaero.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.EditDetail
import com.aquaero.realestatemanager.GeolocMap
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.Loan
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.navigateToDetailEdit
import com.aquaero.realestatemanager.repository.AgentRepository
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.repository.PropertyRepository
import com.aquaero.realestatemanager.utils.AppContentType
import com.google.android.gms.maps.model.LatLng

class AppViewModel(
    private val propertyRepository: PropertyRepository,
) : ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    @SuppressLint("NewApi")
    val fakeProperties = propertyRepository.fakeProperties

    /**
     * Init content type, according to window's width,
     * to choose dynamically, on screen state changes, whether to show
     * just a list content, or both a list and detail content
     */
    fun contentType(windowSize: WindowWidthSizeClass): AppContentType = when (windowSize) {
        WindowWidthSizeClass.Expanded -> {
            AppContentType.SCREEN_WITH_DETAIL
        }

        else -> {
            AppContentType.SCREEN_ONLY
        }
    }

    /**
     * TopBar
     */

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

    fun onClickMenu(
        currentScreen: String?,
        navController: NavHostController,
        propertyId: Comparable<*>
    ) {
        when (currentScreen) {

            ListAndDetail.routeWithArgs, Detail.routeWithArgs -> {
                Log.w("Click on menu edit", "Property $propertyId")
                navController.navigateToDetailEdit(propertyId.toString())
            }

            EditDetail.routeWithArgs, SearchCriteria.route, Loan.route -> {
                Log.w("Click on menu valid", "Property $propertyId")
                // TODO: Replace toast with specific action
                Toast
                    .makeText(
                        context, "Click on ${context.getString(R.string.valid)}",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    val onClickRadioButton = { currency: String ->
        Toast
            .makeText(context, "Click on $currency", Toast.LENGTH_SHORT)
            .show()
    }

    /** End TopBar */




}



