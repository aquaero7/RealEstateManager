package com.aquaero.realestatemanager.ui.components.app

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.aquaero.realestatemanager.Detail
import com.aquaero.realestatemanager.ListAndDetail
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.utils.AppContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    context: Context,
    currentScreen: String?,
    contentType: AppContentType
) {
    val dollar = stringResource(id = R.string.dollar)
    val euro = stringResource(id = R.string.euro)
    var selectedOption by remember { mutableStateOf(dollar) }
    var expanded by remember { mutableStateOf(false) }
    val itemEnabled = (currentScreen == Detail.routeWithArgs)
            || (currentScreen == ListAndDetail.routeWithArgs
            && contentType == AppContentType.SCREEN_WITH_DETAIL)

    TopAppBar(
        modifier = modifier.height(48.dp),
        title = { Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 4.dp)
        ) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            // containerColor = MaterialTheme.colorScheme.primaryContainer,
            // containerColor = Color.Cyan,
        ),
        actions = {
            // RadioButton €
            TopBarRadioButton(
                selected = selectedOption == euro,
                onClick = { selectedOption = euro },
                text = euro,
            )
            // RadioButton $
            TopBarRadioButton(
                selected = selectedOption == dollar,
                onClick = { selectedOption = dollar },
                text = dollar,
            )

            // Dropdown menu
            Box(modifier = Modifier
                .width(64.dp)
                .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.cd_more),
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TopBarDropDownMenuItem(
                        imageVector = Icons.Default.Upload,
                        contentDesc = stringResource(id = R.string.cd_export),
                        text = { Text(stringResource(id = R.string.export)) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Click on Export", Toast.LENGTH_SHORT).show()
                        },
                        enabled = true,
                    )
                    TopBarDropDownMenuItem(
                        imageVector = Icons.Default.AddHome,
                        contentDesc = stringResource(id = R.string.cd_create),
                        text = { Text(stringResource(id = R.string.create)) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Click on Create", Toast.LENGTH_SHORT).show()
                        },
                        enabled = true,
                    )
                    TopBarDropDownMenuItem(
                        imageVector = Icons.Default.Edit,
                        contentDesc = stringResource(id = R.string.cd_edit),
                        text = { Text(stringResource(id = R.string.edit)) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Click on Edit", Toast.LENGTH_SHORT).show()
                        },
                        enabled = itemEnabled,
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

@Composable
fun TopBarDropDownMenuItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDesc: String,
    text: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    enabled: Boolean

) {
    DropdownMenuItem(
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDesc,
            )
        },
        text = text,
        onClick = onClick,
        enabled = enabled,
    )
}