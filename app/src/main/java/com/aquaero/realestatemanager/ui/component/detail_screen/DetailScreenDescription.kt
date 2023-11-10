package com.aquaero.realestatemanager.ui.component.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenDescription(
    property: Property
) {
    Text(
        text = stringResource(R.string.description),
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp)
    )
    Column(
        modifier = Modifier
            .height(120.dp)
            .padding(top = 8.dp, start = 8.dp, end = 12.dp)
            .verticalScroll(
                state = rememberScrollState(),
                enabled = true
            )
    ) {
        property.description?.let {
            Text(
                text = it,
                textAlign = TextAlign.Justify,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                modifier = Modifier
                    .fillMaxHeight()
                // .padding(top = 8.dp)
                // .padding(horizontal = 8.dp)
            )
        }
    }
}