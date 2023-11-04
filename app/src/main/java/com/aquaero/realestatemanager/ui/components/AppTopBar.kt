package com.aquaero.realestatemanager.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaero.realestatemanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(context: Context) {
    val dollar = stringResource(id = R.string.dollar)
    val euro = stringResource(id = R.string.euro)
    var selectedOption by remember { mutableStateOf(dollar) }
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
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
        modifier = Modifier.height(48.dp),
        actions = {
            // RadioButton
            Box(/* contentAlignment = Alignment.Center */) {
                RadioButton(
                    selected = selectedOption == euro,
                    onClick = { selectedOption = euro },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Yellow,
                        unselectedColor = Color.Gray,
                        disabledSelectedColor = Color.Gray,
                        disabledUnselectedColor = Color.Gray
                    ),
                )
                Text(
                    text = euro,
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }

            // RadioButton
            Box(/* contentAlignment = Alignment.Center */) {
                RadioButton(
                    selected = selectedOption == dollar,
                    onClick = { selectedOption = dollar },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Yellow,
                        unselectedColor = Color.Gray,
                        disabledSelectedColor = Color.Gray,
                        disabledUnselectedColor = Color.Gray
                    ),
                )
                Text(
                    text = dollar,
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }

            // Dropdown menu
            Box(modifier = Modifier
                .width(64.dp)
                .wrapContentSize(Alignment.TopEnd)) {
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
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = stringResource(id = R.string.cd_export)
                            )
                        },
                        text = { Text(stringResource(id = R.string.export)) },
                        onClick = {
                            Toast.makeText(context, "Click on Export", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    )
}
