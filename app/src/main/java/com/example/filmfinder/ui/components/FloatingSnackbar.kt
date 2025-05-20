package com.example.filmfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * A floating snackbar that avoids the keyboard.
 * Adaptive positioning - shows at top when keyboard is visible.
 */
@Composable
fun FloatingSnackbar(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showAtTop: Boolean = false,
    duration: Long = 4000 // Default 4 seconds
) {
    val view = LocalView.current
    val isKeyboardOpen = remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    // Check if keyboard is visible
    DisposableEffect(view) {
        val listener = ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            isKeyboardOpen.value = imeVisible
            insets
        }

        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }

    // Auto-dismiss effect
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            kotlinx.coroutines.delay(duration)
            onDismiss()
        }
    }

    if (message.isNotEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = if (isKeyboardOpen.value || showAtTop) Alignment.TopCenter else Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.inverseSurface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(
                        onClick = {
                            // Hide keyboard if it's open when dismissing
                            if (isKeyboardOpen.value) {
                                localFocusManager.clearFocus()
                            }
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "Dismiss",
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    }
}