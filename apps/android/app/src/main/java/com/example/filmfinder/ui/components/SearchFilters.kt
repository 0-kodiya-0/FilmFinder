package com.example.filmfinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.remote.model.MovieFilter

/**
 * A simplified filter panel component that allows users to set various search filters.
 */
@Composable
fun FilterPanel(
    currentFilter: MovieFilter,
    onFilterChanged: (MovieFilter) -> Unit,
    onApplyFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Filter header with expand/collapse button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filters",
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Search Filters",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expanded filter options
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // Year Filter - using the fixed version
                YearFilter(
                    year = currentFilter.year,
                    onYearChanged = { newYear ->
                        onFilterChanged(currentFilter.copy(year = newYear))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Include Ratings Filter
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = currentFilter.includeRatings,
                        onCheckedChange = { checked ->
                            onFilterChanged(currentFilter.copy(includeRatings = checked))
                        }
                    )

                    Text(
                        text = "Include Ratings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apply filters button
                Button(
                    onClick = onApplyFilter,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}

/**
 * Year filter numeric input - fixed to allow proper text input
 */
@Composable
fun YearFilter(
    year: Int?,
    onYearChanged: (Int?) -> Unit
) {
    // Store input text separately from the year value
    var inputText by remember { mutableStateOf(year?.toString() ?: "") }

    Column {
        Text(
            text = "Year",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { newText ->
                // Update text state first
                inputText = newText

                // Then update the year value if valid
                if (newText.isBlank()) {
                    onYearChanged(null)
                } else {
                    val parsedYear = newText.toIntOrNull()
                    if (parsedYear != null && parsedYear in 1800..2030) {
                        onYearChanged(parsedYear)
                    }
                }
            },
            placeholder = { Text("Enter year (optional)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}