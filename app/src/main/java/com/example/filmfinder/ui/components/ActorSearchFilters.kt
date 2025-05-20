package com.example.filmfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.remote.model.ActorSearchFilter

/**
 * A reusable filter panel component for actor search.
 * This component can be used in any screen that needs actor filtering capabilities.
 */
@Composable
fun ActorSearchFilterPanel(
    filter: ActorSearchFilter,
    onFilterChange: (ActorSearchFilter) -> Unit,
    onApplyFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Filter Actor Results",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Genre filter
            GenreFilter(
                genre = filter.genre,
                onGenreChange = { onFilterChange(filter.copy(genre = it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Year range filter
            YearRangeFilter(
                fromYear = filter.fromYear,
                toYear = filter.toYear,
                onFromYearChange = { onFilterChange(filter.copy(fromYear = it)) },
                onToYearChange = { onFilterChange(filter.copy(toYear = it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Rating filter switch
            RatingFilter(
                highRatedOnly = filter.highRatedOnly,
                onToggleHighRated = { onFilterChange(filter.copy(highRatedOnly = it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apply filter button
            Button(
                onClick = onApplyFilter,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Apply Filters")
            }
        }
    }
}

/**
 * Genre filter input component
 */
@Composable
private fun GenreFilter(
    genre: String,
    onGenreChange: (String) -> Unit
) {
    Text(
        text = "Genre",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        value = genre,
        onValueChange = onGenreChange,
        placeholder = { Text("Any genre") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

/**
 * Year range filter component with from/to fields
 */
@Composable
private fun YearRangeFilter(
    fromYear: Int?,
    toYear: Int?,
    onFromYearChange: (Int?) -> Unit,
    onToYearChange: (Int?) -> Unit
) {
    // Convert year values to strings for the text fields
    val fromYearText = remember(fromYear) { fromYear?.toString() ?: "" }
    val toYearText = remember(toYear) { toYear?.toString() ?: "" }

    // Create mutable state for text field values
    var fromYearValue by remember { mutableStateOf(fromYearText) }
    var toYearValue by remember { mutableStateOf(toYearText) }

    // Update text field values when the props change
    LaunchedEffect(fromYearText) { fromYearValue = fromYearText }
    LaunchedEffect(toYearText) { toYearValue = toYearText }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "From Year",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = fromYearValue,
                onValueChange = {
                    fromYearValue = it
                    onFromYearChange(it.toIntOrNull())
                },
                placeholder = { Text("From") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "To Year",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = toYearValue,
                onValueChange = {
                    toYearValue = it
                    onToYearChange(it.toIntOrNull())
                },
                placeholder = { Text("To") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

/**
 * Rating filter toggle component
 */
@Composable
private fun RatingFilter(
    highRatedOnly: Boolean,
    onToggleHighRated: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Show High Rated Only (IMDb ≥ 7.0)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = highRatedOnly,
            onCheckedChange = onToggleHighRated
        )
    }
}