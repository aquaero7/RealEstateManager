package com.aquaero.realestatemanager.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.data.fakeProperties
import com.aquaero.realestatemanager.ui.components.PropertyCard

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    onPropertyClick: (Long) -> Unit = {}
) {
    /*
    Column {
        Text(text = "ListScreen")
    }
    */

    LazyColumn(
        modifier = Modifier
            // .padding(vertical = 20.dp)
            .fillMaxWidth()
    ) {
        item { Text(
            text = "ListScreen",
            Modifier.padding(vertical = 20.dp)
        ) }

        items(items = fakeProperties) { property ->
            /*
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // onPropertyClick(property)
                        onPropertyClick(property.pId)
                    }
            ) {
                Text(text = property.pId.toString(), Modifier.padding(vertical = 40.dp))
            }
            */
            //
            PropertyCard(
                property.pId,
                property.pType,
                property.pAddress.city,
                property.pPrice,
                // property.photos?.get(0)?.phId,
                R.drawable.ic_launcher_background.toLong(),
                onPropertyClick
            )
            //
        }
    }
}