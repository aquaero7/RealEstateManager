package com.aquaero.realestatemanager.ui.component.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Black
import com.aquaero.realestatemanager.ui.theme.Pink40
import com.aquaero.realestatemanager.ui.theme.RealEstateManagerTheme
import com.aquaero.realestatemanager.ui.theme.White

@Composable
fun DetailScreenInformationItem(
    image: ImageVector,
    contentDesc: String,
    label: String,
    value: String,
    suffix: String? = null,
    iconColor: Color = MaterialTheme.colorScheme.tertiary, // Pink40,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueBackgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        // Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentHeight()
        ) {
            Icon(
                imageVector = image,
                contentDescription = contentDesc,
                tint = iconColor,
                modifier = Modifier.size(40.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentHeight()
        ) {
            // Label
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary, // .onSurface,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
            // Data value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(valueBackgroundColor)
            ) {
                Text(
                    text = value,
                    fontSize = 12.sp,
                    color = valueColor,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                if (suffix != null) {
                    Text(
                        text = suffix,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    RealEstateManagerTheme {
        DetailScreenInformationItem(
            image = Icons.Default.Person,
            contentDesc = "contentDescription",
            label = "label",
            value = "value")
    }
}
