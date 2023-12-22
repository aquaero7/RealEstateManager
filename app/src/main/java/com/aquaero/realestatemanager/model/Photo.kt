package com.aquaero.realestatemanager.model

import android.net.Uri

data class Photo(
    val phId: Long,
    val phUri: Uri,
    var phLabel: String
)
