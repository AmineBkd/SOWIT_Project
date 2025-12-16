package com.example.sowit.ui.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.sowit.R
import com.example.sowit.ui.map.uiModel.ZoneClusterItem
import java.util.Locale

private val bottomSheetHeight = 200.dp

@Composable
fun BottomSheetContent(
    zoneClusters: List<ZoneClusterItem>,
    onItemClick: (ZoneClusterItem) -> Unit = {},
    onDeleteItemClick: (ZoneClusterItem) -> Unit = {},
) {
    HorizontalDivider()
    Column(
        modifier = Modifier
            .height(bottomSheetHeight)
            .innerShadow(
                shape = RectangleShape,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.1f),
                    radius = 3.dp,
                    offset = DpOffset(x = 0.dp, 3.dp)
                )
            )
    ) {
        if (zoneClusters.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_plots),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn {
                items(count = zoneClusters.size) {
                    val zoneCluster = zoneClusters[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onItemClick(zoneCluster) }),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)) {
                            Text(
                                text = zoneCluster.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = stringResource(
                                    R.string.plot_area, String.format(
                                        Locale.ENGLISH,
                                        "%.2f",
                                        zoneCluster.getArea()
                                    )
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Button(
                            modifier = Modifier.padding(end = 15.dp),
                            onClick = { onDeleteItemClick(zoneCluster) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.x),
                                contentDescription = null
                            )
                        }

                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        zoneClusters = listOf(
            ZoneClusterItem(title = "Plot 1"),
            ZoneClusterItem(title = "Plot 2"),
            ZoneClusterItem(title = "Plot 3"),
        )
    )
}