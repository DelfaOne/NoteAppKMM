package com.example.noteappkmm.android.notelist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteappkmm.android.MyApplicationTheme

@Composable
fun TopBar(
    text: String,
    isSearchActive: Boolean,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCloseClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.padding(end = 4.dp)
    ) {
        HideableSearchTextField(
            text = text,
            isSearchActive = isSearchActive,
            onTextChange = onTextChange,
            onSearchClick = onSearchClick,
            onCloseClick = onCloseClick,
            modifier.weight(1f)
        )
        IconButton(onClick = onFilterClick) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Close search",
                modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    MyApplicationTheme {
        TopBar(
            text = "Preview",
            isSearchActive = true,
            onTextChange = { },
            onSearchClick = { },
            onCloseClick = { },
            onFilterClick = { }
        )
    }
}