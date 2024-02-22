package com.example.noteappkmm.android.notedetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.noteappkmm.android.notedetail.NoteDetailViewModel

@Composable
fun NoteDetailScreen(
    noteId: Long,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val noteIsSaved by viewModel.noteIsSaved.collectAsState()
    
    LaunchedEffect(key1 = noteIsSaved) {

    }

}