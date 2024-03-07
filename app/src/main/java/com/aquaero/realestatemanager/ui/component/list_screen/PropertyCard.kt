package com.aquaero.realestatemanager.ui.component.list_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.aquaero.realestatemanager.AppContentType
import com.aquaero.realestatemanager.NO_PHOTO_ID
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.ui.theme.Magenta
import com.aquaero.realestatemanager.ui.theme.Red
import com.aquaero.realestatemanager.ui.theme.Yellow

@Composable
fun PropertyCard(
    contentType: AppContentType,
    pId: Long,
    pType: String,
    pCity: String,
    phId: Long,
    phUri: String,
    pPriceFormatted: String,
    isSold: Boolean,
    selected: Boolean = false,
    unselectedByDefaultDisplay: Boolean = false,
    onSelection: () -> Unit,
    onPropertyClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 8.dp,
                vertical = 0.dp
            ) // Vertical padding handled by LazyRow verticalArrangement
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                if (selected && contentType == AppContentType.LIST_AND_DETAIL
                    && !unselectedByDefaultDisplay
                ) BorderStroke(width = 2.dp, color = Yellow)
                else BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            )
            .clickable {
                onSelection()
                onPropertyClick(pId)
            },
        shape = RoundedCornerShape(0.dp), // or MaterialTheme.shapes.medium // or CutCornerShape(topEnd = 10.dp)
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors()
    ) {
        Box {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resourceUri =
                    if (phId != NO_PHOTO_ID) phUri else R.drawable.baseline_photo_camera_black_24
                val colorFilter =
                    if (phId != NO_PHOTO_ID) null else ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                val alpha = if (phId != NO_PHOTO_ID) 1F else 0.2F

                Image(
                    painter = rememberAsyncImagePainter(resourceUri),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = colorFilter,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(0.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .alpha(alpha),
                )
                Surface(
                    color = if (
                        selected && contentType == AppContentType.LIST_AND_DETAIL
                        && !unselectedByDefaultDisplay
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
                                text = pPriceFormatted,
                                style = MaterialTheme.typography.titleLarge,
                                color = Red,
                            )
                        }
                    }
                }
            }

            if (isSold) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterEnd),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(84.dp)
                            .alpha(0.7F)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = CircleShape
                            )
                            .background(color = Magenta, shape = CircleShape)
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                    ) {
                        Text(
                            color = Yellow,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            text = "* * *\n${stringResource(id = R.string.sold).uppercase()}\n* * *",
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PropertyCardPreview() {
    PropertyCard(
        contentType = AppContentType.LIST_OR_DETAIL,
        pId = 1,
        pType = TypeEnum.UNASSIGNED.key,
        pCity = "City",
        phId = 1,
        phUri = "",
        pPriceFormatted = "000 000 €",
        isSold = true,
        onSelection = {  },
        onPropertyClick = {  },
    )
}
