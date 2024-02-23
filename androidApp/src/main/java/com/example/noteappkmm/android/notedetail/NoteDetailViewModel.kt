package com.example.noteappkmm.android.notedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.note.NoteDataSource
import com.example.noteappkmm.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteTitle = savedStateHandle.getStateFlow(NOTE_TITLE_KEY, "")
    private val isNoteTitleFocused = savedStateHandle.getStateFlow(IS_NOTE_TITLE_FOCUSED_KEY, false)
    private val noteContent = savedStateHandle.getStateFlow(NOTE_CONTENT_KEY, "")
    private val isNoteContentFocused = savedStateHandle.getStateFlow(IS_NOTE_CONTENT_FOCUSED_KEY, false)
    private val noteColor = savedStateHandle.getStateFlow(NOTE_COLOR_KEY, Note.generateRandomColor())

    private val _noteIsSaved = MutableStateFlow(false)
    val noteIsSaved = _noteIsSaved.asStateFlow()

    private var existingNoteId: Long? = null

    val state = combine(
        noteTitle,
        isNoteTitleFocused,
        noteContent,
        isNoteContentFocused,
        noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintVisible = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintVisible = content.isEmpty() && !isContentFocused,
            noteColor = color
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    init {
        savedStateHandle.get<Long>("noteId")?.let { existingNoteId ->
            if (existingNoteId == -1L) {
                return@let
            }
            this.existingNoteId = existingNoteId
            viewModelScope.launch(Dispatchers.IO) {
                noteDataSource.getNoteById(existingNoteId)?.let { note ->
                    savedStateHandle[NOTE_TITLE_KEY] = note.title
                    savedStateHandle[NOTE_CONTENT_KEY] = note.content
                    savedStateHandle[NOTE_COLOR_KEY] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle[NOTE_TITLE_KEY] = text
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle[NOTE_CONTENT_KEY] = text
    }

    fun onNoteTitleFocusChanged(isNoteFocused: Boolean) {
        savedStateHandle[IS_NOTE_TITLE_FOCUSED_KEY] = isNoteFocused
    }

    fun onNoteContentFocusChanged(isContentFocused: Boolean) {
        savedStateHandle[IS_NOTE_CONTENT_FOCUSED_KEY] = isContentFocused
    }

    fun saveNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDataSource.insertNote(
                Note(
                    id = existingNoteId,
                    title = noteTitle.value,
                    content = noteContent.value,
                    colorHex = noteColor.value,
                    created = DateTimeUtil.now()
                )
            )
            _noteIsSaved.value = true
        }
    }


    companion object {
        const val NOTE_TITLE_KEY = "noteTitle"
        const val IS_NOTE_TITLE_FOCUSED_KEY = "isNoteTitleFocused"
        const val NOTE_CONTENT_KEY = "noteContent"
        const val IS_NOTE_CONTENT_FOCUSED_KEY = "isNoteContentFocused"
        const val NOTE_COLOR_KEY = "noteColor"
    }
}