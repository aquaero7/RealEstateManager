package com.aquaero.realestatemanager.ui.component.detail_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.ui.theme.Gray66Trans66

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailScreenMedia(
    property: Property
) {
    Text(
        text = stringResource(R.string.media),
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
    )
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .padding(top = 4.dp, start = 4.dp, end = 6.dp)
    ) {
        items(items = property.photos as List<Photo>) { photo ->
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Image(
                    // painter = painterResource(id = photo.phId.toInt()),
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .padding(0.dp)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(0.dp),
                    color = Gray66Trans66,
                ) {
                    Text(
                        text = photo.phLabel,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}