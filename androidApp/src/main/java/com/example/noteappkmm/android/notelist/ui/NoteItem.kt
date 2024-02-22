package com.example.noteappkmm.android.notelist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteappkmm.android.MyApplicationTheme
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.time.DateTimeUtil
import com.example.noteappkmm.presentation.RedOrangeHex

@Composable
fun NoteItem(
    note: Note,
    backgroundColor: Color,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatedDate = remember(note.created) {
        DateTimeUtil.formatNoteDate(note.created)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .clickable { onNoteClick() }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete note",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() }, //TODO change this
                        indication = null
                    ) {
                        onDeleteClick()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = note.content, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = formatedDate,
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.End)
        )

    }
}

@Preview
@Composable
fun NoteItemPreview() {
    val color = RedOrangeHex
    MyApplicationTheme {
       NoteItem(
           note = Note(
               1,
               "Title preview",
               "Content preview",
               color,
               DateTimeUtil.now()
           ),
           backgroundColor = Color(color),
           onNoteClick = { /*TODO*/ },
           onDeleteClick = { /*TODO*/ })
    }
}