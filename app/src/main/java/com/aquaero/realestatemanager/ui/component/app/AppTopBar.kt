package com.aquaero.realestatemanager.ui.component.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    menuIcon: ImageVector,
    menuIconContentDesc: Int,
    menuEnabled: Boolean,
    onClickMenu: () -> Unit,
    onClickRadioButton: (Int) -> Unit,
    currency: String,
) {
    val colors: TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
    val titleText: String = stringResource(id = R.string.app_name)
    val dollar: Int = R.string.dollar
    val stringDollar = stringResource(id = dollar)
    val euro: Int = R.string.euro
    val stringEuro = stringResource(id = euro)
    var selectedOption by remember(currency) { mutableStateOf(currency) }

    TopAppBar(
        modifier = modifier.height(48.dp),
        title = { Text(
            text = titleText,
            color = White,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 4.dp)
        ) },
        colors = colors,
        actions = {
            // RadioButton €
            TopBarRadioButton(
                selected = selectedOption == stringEuro,
                onClick = { selectedOption = stringEuro; run { onClickRadioButton(euro) } },
                text = stringEuro,
            )
            // RadioButton $
            TopBarRadioButton(
                selected = selectedOption == stringDollar,
                onClick = { selectedOption = stringDollar; run { onClickRadioButton(dollar) } },
                text = stringDollar,
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
                        contentDescription = stringResource(id = menuIconContentDesc),
                        tint = White.copy(alpha = LocalContentColor.current.alpha),
                        // The alpha parameter makes the icon fade to gray when disabled
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
    onClick: () -> Unit,
    text: String,
) {
    Box(
        modifier = modifier,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = White,
                unselectedColor = White,
                disabledSelectedColor = White.copy(alpha = LocalContentColor.current.alpha),
                disabledUnselectedColor = White.copy(alpha = LocalContentColor.current.alpha),
            ),
        )
        Text(
            text = text,
            color = White,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 36.dp)
        )
    }
}
