package com.example.demo.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo.data.RegionInfo
import com.example.demo.data.RegionPreferences

@Composable
fun RegionOverflowMenu(
    currentRegion: RegionInfo?,
    showRegionMenu: Boolean,
    onToggleMenu: () -> Unit,
    onSelectRegion: (String) -> Unit,
    onClearOverride: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onToggleMenu,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More options",
        )
    }

    DropdownMenu(
        expanded = showRegionMenu,
        onDismissRequest = onToggleMenu,
    ) {
        Text(
            text =
                "Region: ${currentRegion?.name ?: "Unknown"}" +
                    if (currentRegion?.isOverride == true) " (Override)" else "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        HorizontalDivider()

        RegionPreferences.AVAILABLE_REGIONS.forEach { region ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(region.name)
                        if (currentRegion?.code == region.code) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
                onClick = { onSelectRegion(region.code) },
            )
        }

        if (currentRegion?.isOverride == true) {
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Use Auto-detected") },
                onClick = onClearOverride,
            )
        }
    }
}
