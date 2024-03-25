package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.Field
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.SearchCriteria
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.utils.convertEuroToDollar

class SearchViewModel(

): ViewModel() {

    val itemPois: MutableList<Poi> = mutableListOf()

    fun onClickMenu(context: Context) {
        // TODO

    }

    fun onFieldValueChange(field: String, fieldType: String?, value: String, currency: String) {
        val digitalValue =
            if (field != Field.DESCRIPTION.name && value.isNotEmpty() && value.isDigitsOnly()) value.toLong() else null
        val price: Int?
        when (field) {
            Field.SURFACE.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }
            Field.ROOMS.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }
            Field.BATHROOMS.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }
            Field.BEDROOMS.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }
            Field.DESCRIPTION.name -> { Log.w("SearchViewModel", "$field = $value") }
            Field.REGISTRATION_DATE.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }
            Field.SALE_DATE.name -> { Log.w("SearchViewModel", "$field: $fieldType = $value") }

            Field.CITY.name -> { Log.w("SearchViewModel", "$field = $value") }
            Field.STATE.name -> { Log.w("SearchViewModel", "$field = $value") }
            Field.ZIP_CODE.name -> { Log.w("SearchViewModel", "$field = $value") }
            Field.COUNTRY.name -> { Log.w("SearchViewModel", "$field = $value") }

            Field.PRICE.name -> {
                Log.w("SearchViewModel", "$field: $fieldType = $value $currency")
                price =
                    if (value.isNotEmpty() && value.isDigitsOnly()) {
                        when (currency) {
                            "€" -> convertEuroToDollar(euros = value.toInt())
                            else -> value.toInt()
                        }!!
                    } else null
            }
        }
    }

    fun onDropdownMenuValueChange(
        value: String,
        types: MutableList<Type>,
        agents: MutableList<Agent>,
    ) {
        val typeId: String
        val agentId: Long
        val category = value.substringBefore(delimiter = "#", missingDelimiterValue = "")
        val index = value.substringAfter(delimiter = "#", missingDelimiterValue = value).toInt()
        when (category) {
            DropdownMenuCategory.TYPE.name -> {
                typeId = types.elementAt(index).typeId
                Log.w("SearchViewModel", "TypeId = $typeId")
            }
            DropdownMenuCategory.AGENT.name -> {
                agentId = agents.elementAt(index).agentId
                Log.w("SearchViewModel", "AgentId = $agentId")
            }
        }
    }

    fun onPoiClick(poiItem: String, isSelected: Boolean) {
        if (isSelected) itemPois.add(Poi(poiId = poiItem)) else itemPois.remove(Poi(poiId = poiItem))
        Log.w("SearchViewModel", "itemPois contains ${itemPois.size} items")

        itemPois.forEach() { Log.w("SearchViewModel", it.poiId) }
    }

    fun onSalesRadioButtonClick(button: String) {
        Log.w("SearchViewModel", "Sales selection is: $button")
    }

    fun onPhotosRadioButtonClick(button: String) {
        Log.w("SearchViewModel", "Photos selection is: $button")
    }


}