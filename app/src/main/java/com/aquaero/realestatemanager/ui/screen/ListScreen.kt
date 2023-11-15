package com.aquaero.realestatemanager.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.component.list_screen.PropertyCard
import com.aquaero.realestatemanager.ui.theme.Pink40
import com.aquaero.realestatemanager.utils.AppContentType
import com.aquaero.realestatemanager.viewmodel.ListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    listViewModel: ListViewModel,
    contentType: AppContentType,
    property: Property,
    onPropertyClick: (Long) -> Unit,
    onFabClick: () -> Unit,
) {
    Scaffold (
        // floatingActionButtonPosition = FabPosition.End, // = default
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
            ) {
                Icon(
                    modifier = Modifier.scale(1.0F),
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.cd_fab),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            var selectedId by remember { mutableLongStateOf(-1L) }  // For compatibility with ListAndDetailScreen
            val items = listViewModel.fakeProperties

            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    // .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                state = LazyListState(firstVisibleItemIndex = items.indexOf(property)),
            ) {
                items(items = items) { propertyItem ->
                    PropertyCard(
                        pId = propertyItem.pId,
                        pType = propertyItem.pType,
                        pCity = propertyItem.pAddress.city,
                        pPrice = propertyItem.pPrice,
                        // phId = propertyItem.photos?.get(0)?.phId,        // TODO : When implemented
                        phId = R.drawable.ic_launcher_background.toLong(),  // TODO: Provisional
                        contentType = contentType,
                        selected = selectedId == propertyItem.pId ||
                                property.pId.toString() == propertyItem.pId.toString(), // For compatibility with ListAndDetailScreen
                        unselectedByDefaultDisplay =
                                property.pId.toString() != propertyItem.pId.toString(), // For compatibility with ListAndDetailScreen
                        onSelection = { selectedId = propertyItem.pId },                // For compatibility with ListAndDetailScreen
                        onPropertyClick = onPropertyClick,
                    )
                }
            }
        }
    }
}