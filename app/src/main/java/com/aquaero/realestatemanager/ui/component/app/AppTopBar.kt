package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    menuIcon: ImageVector,
    menuIconContentDesc: String,
    menuEnabled: Boolean,
    onClickMenu: () -> Unit,
    onClickRadioButton: (String) -> Unit,
    titleText: String = stringResource(id = R.string.app_name),
    colors: TopAppBarColors = TopAppBarDefaults
        .topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
    dollar: String = stringResource(id = R.string.dollar),
    euro: String = stringResource(id = R.string.euro),
) {
    var selectedOption by remember { mutableStateOf(dollar) }

    TopAppBar(
        modifier = modifier.height(48.dp),
        title = { Text(
            text = titleText,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 4.dp)
        ) },
        colors = colors,
        actions = {
            // RadioButton €
            TopBarRadioButton(
                selected = selectedOption == euro,
                onClick = { selectedOption = euro; run { onClickRadioButton(euro) } },
                text = euro,
            )
            // RadioButton $
            TopBarRadioButton(
                selected = selectedOption == dollar,
                onClick = { selectedOption = dollar; run { onClickRadioButton(dollar) } },
                text = dollar,
            )

            // Menu
            Box(modifier = Modifier
                .width(64.dp)
                .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = onClickMenu,
                    enabled = menuEnabled,
                ) {
                    Icon(
                        imageVector = menuIcon,
                        contentDescription = menuIconContentDesc,
                        tint = Color.White,
                    )
                }
            }
        }
    )
}

@Composable
fun TopBarRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit = {},
    text: String,
) {
    Box(
        modifier = modifier,
        // contentAlignment = Alignment.Center
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Yellow,
                unselectedColor = Color.Gray,
                disabledSelectedColor = Color.Gray,
                disabledUnselectedColor = Color.Gray
            ),
        )
        Text(
            text = text,
            color = Color.Yellow,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 36.dp)
        )
    }
}
