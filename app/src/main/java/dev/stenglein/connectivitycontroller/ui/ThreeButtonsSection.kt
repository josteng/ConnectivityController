package dev.stenglein.connectivitycontroller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.stenglein.alarmairplanetoggle.ui.SectionEntry
import dev.stenglein.connectivitycontroller.ui.theme.ConnectivityControllerTheme


@Composable
fun ThreeButtonsSection(
    title: String,
    description: String,
    button1Text: String,
    button1OnClick: () -> Unit,
    button2Text: String,
    button2OnClick: () -> Unit,
    button3Text: String,
    button3OnClick: () -> Unit,
) {
    SectionEntry(title = title, description = description) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = button1OnClick, modifier = Modifier.weight(1f)) {
                Text(button1Text)
            }
            OutlinedButton(onClick = button2OnClick, modifier = Modifier.weight(1f)) {
                Text(button2Text)
            }
            OutlinedButton(onClick = button3OnClick, modifier = Modifier.weight(1f)) {
                Text(button3Text)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SectionThreeButtonsPreview() {
    ConnectivityControllerTheme {
        ThreeButtonsSection(
            title = "Title",
            description = "Description " + LoremIpsum(30).values.joinToString(" "),
            button1Text = "Button 1",
            button1OnClick = {},
            button2Text = "Button 2",
            button2OnClick = {},
            button3Text = "Button 3",
            button3OnClick = {}
        )
    }
}