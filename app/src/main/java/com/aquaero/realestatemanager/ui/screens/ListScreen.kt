package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.ui.components.PropertyCard
import com.aquaero.realestatemanager.utils.AppContentType

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    contentType: AppContentType,
    onPropertyClick: (Long) -> Unit = {}
) {
    Column {
        Text(text = "ListScreen")

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                // .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            /*
            item { Text(
                text = "ListScreen",
                Modifier.padding(vertical = 20.dp)
            ) }
            */

            items(items = fakeProperties) { property ->
                PropertyCard(
                    property.pId,
                    property.pType,
                    property.pAddress.city,
                    property.pPrice,
                    // property.photos?.get(0)?.phId,
                    R.drawable.ic_launcher_background.toLong(),
                    contentType,
                    onPropertyClick
                )
            }
        }
    }
}