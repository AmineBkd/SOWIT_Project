package com.example.sowit.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.sowit.R

private val mapMenuHeight = 45.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMenu(
    canSavePlot: Boolean = false,
    savePlotClick: (String) -> Unit = {},
    clearPlotClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var saveClick by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .heightIn(max = mapMenuHeight)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painterResource(R.drawable.three_dots_vertical),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.save_plot)) },
                    onClick = { saveClick = true },
                    enabled = canSavePlot
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.clear_markers)) },
                    onClick = {
                        clearPlotClick()
                        expanded = false
                    }
                )
            }
        }

        if (saveClick && canSavePlot) {
            BasicAlertDialog(
                onDismissRequest = { saveClick = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                ),
                content = {
                    SavePopUpContent(
                        onSaveClick = {
                            if(canSavePlot) {
                                savePlotClick(it)
                                saveClick = false
                            }
                        },
                    )
                }
            )
        }
    }
}

@Composable
private fun SavePopUpContent(onSaveClick: (name: String) -> Unit = {}) {
    var textFieldValue by remember { mutableStateOf("") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.plot_name),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    value = textFieldValue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    onValueChange = { value ->
                        textFieldValue = value
                    }
                )
                Button(
                    onClick = {
                        if(textFieldValue.isNotEmpty()) {
                            onSaveClick(textFieldValue)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_button))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MapMenuPreview() {
    MapMenu()
}

@Preview
@Composable
private fun SavePopUpContentPreview() {
    SavePopUpContent()
}