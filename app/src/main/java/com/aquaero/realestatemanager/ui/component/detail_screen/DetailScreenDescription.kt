package com.aquaero.realestatemanager.ui.component.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.theme.Black

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenDescription(
    description: String?
) {
    Text(
        text = stringResource(R.string.description),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
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
        description?.let {
            Text(
                text = it,
                // color = MaterialTheme.colorScheme.onSurface,
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