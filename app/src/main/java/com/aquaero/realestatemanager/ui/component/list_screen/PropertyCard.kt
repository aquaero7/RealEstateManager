package com.aquaero.realestatemanager.ui.component.list_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.ui.theme.Yellow
import com.aquaero.realestatemanager.utils.AppContentType

@Composable
fun PropertyCard(
    pId: Long,
    pType: String,
    pCity: String,
    pPrice: Int,
    phId: Long?,
    contentType: AppContentType,
    selected: Boolean = false,
    unselectedByDefaultDisplay: Boolean = false,
    onSelection: () -> Unit,
    onPropertyClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                if (selected) BorderStroke(width = 2.dp, color = Yellow)
                else BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            )
            .clickable {
                onSelection()
                onPropertyClick(pId) },
        shape = RoundedCornerShape(0.dp),   // = MaterialTheme.shapes.medium // = CutCornerShape(topEnd = 10.dp)
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (phId != null) {
                Image(
                    painter = painterResource(id = phId.toInt()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(0.dp),
                    contentScale = ContentScale.Fit
                )
                Surface(
                    color = if (
                        selected && contentType == AppContentType.SCREEN_WITH_DETAIL &&
                        !unselectedByDefaultDisplay
                        ) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxSize(),
                    ) {
                    Column(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(horizontal = 0.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Column {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                text = pType,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                text = pCity,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                text = pPrice.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = Red,
                                /*
                                color = if (
                                    selected && contentType == AppContentType.SCREEN_WITH_DETAIL &&
                                    !unselectedByDefaultDisplay
                                    ) White else Red,
                                */
                            )
                        }
                    }
                }
            }
        }
    }
}