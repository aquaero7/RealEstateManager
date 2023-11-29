package com.aquaero.realestatemanager.ui.component.map_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.Gray
import com.aquaero.realestatemanager.ui.theme.Red

@Composable
fun MapScreenNoMap() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            modifier = Modifier.size(400.dp),
            imageVector = Icons.Outlined.LocationOff,
            contentDescription = "xxx",
            alignment = Alignment.Center,
            alpha = 0.5F,
            colorFilter = ColorFilter.tint(Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The map is unavailable because\nlocation permissions\nhave been revoked !",
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            fontSize = 20.sp,
            color = Red,
        )
        Spacer(modifier = Modifier.height(40.dp))
    }

}