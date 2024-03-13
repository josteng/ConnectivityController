package dev.stenglein.alarmairplanetoggle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


/**
 * Title text.
 */
@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}


/**
 * Description text.
 */
@Composable
fun DescriptionText(text: String, fontWeight: FontWeight = FontWeight.Normal) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = fontWeight
    )
}


/**
 * Header text for sections.
 */
@Composable
private fun SectionHeadlineText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary
    )
}


/**
 * Base section, e.g. to be used inside the custom scaffold.
 */
@Composable
fun BaseSection(
    headline: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SectionHeadlineText(headline)

        Spacer(modifier = Modifier.height(24.dp))  // Try to match the look of the settings app

        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            content()
        }
    }
}


@Composable
fun SectionEntry(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    additionalContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TitleText(title)
        DescriptionText(description)

        if (additionalContent != null) {
            Spacer(modifier = Modifier.height(4.dp))
            additionalContent()
        }
    }
}


/**
 * Custom scaffold that allows the content to be scrolled under the bottom navigation bar and to
 * draw behind the navigation bar.
 */
@Composable
fun ScaffoldCustom(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    sections: List<@Composable () -> Unit>
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                // Allow navigation bar to overlap content
                modifier = Modifier.padding(0.dp, paddingValues.calculateTopPadding(), 0.dp, 0.dp),
                // Add padding to the bottom to prevent content from being overlapped by the bottom navigation bar
                contentPadding = PaddingValues(
                    16.dp, 16.dp, 16.dp, 16.dp + paddingValues.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                sections.forEach { section ->
                    item {
                        section()
                    }
                }
            }

            NavigationBarBackground()
        }
    }
}


/**
 * Semi-transparent surface for the bottom navigation bar
 */
@Composable
private fun BoxScope.NavigationBarBackground() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .windowInsetsBottomHeight(WindowInsets.navigationBars),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    ) {}
}