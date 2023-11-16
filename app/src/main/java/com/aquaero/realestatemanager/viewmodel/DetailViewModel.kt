package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.SM_MK_COLOR1
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import java.text.Normalizer

class DetailViewModel(

): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    /* TODO: To be deleted
    fun thumbnailUrl(property: Property): String {
        val smMkAddress1 = property.pAddress.toUrl()
        return SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MK_COLOR1 + smMkAddress1 + SM_KEY
    }
    */

}

