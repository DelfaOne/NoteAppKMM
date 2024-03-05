package com.example.noteappkmm.android.notelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.note.NoteDataSource
import com.example.noteappkmm.domain.note.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchNotes = SearchNotesUseCase()

    private val notes = savedStateHandle.getStateFlow(NOTE_KEY, emptyList<Note>())
    private val searchText = savedStateHandle.getStateFlow(SEARCH_TEXT_KEY, "")
    private val isSearchActive = savedStateHandle.getStateFlow(IS_SEARCH_ACTIVE_KEY, false)
    private val isSortedByAscend = savedStateHandle.getStateFlow(IS_SORTED_BY_ASCEND, true)

    private var isSortedByAscendInternal = true

    val state = combine(
        notes,
        searchText,
        isSearchActive,
        isSortedByAscend
    ) { notes, searchText, isSearchActive, _ ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive,
            isSortedByAscend = isSortedByAscendInternal
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    init {
        viewModelScope.launch {
            //noteDataSource.deleteAllNotes()
        }
    }

    fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) {//TODO inject dispatcher
            savedStateHandle["notes"] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch() {
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if (!isSearchActive.value) {
            savedStateHandle["searchText"] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }

    fun onFilterClick() {
        isSortedByAscendInternal = !isSortedByAscendInternal
        savedStateHandle[IS_SORTED_BY_ASCEND] = isSortedByAscendInternal

        viewModelScope.launch(Dispatchers.IO) {
            when (isSortedByAscend.value) {
                true -> savedStateHandle["notes"] = noteDataSource.getAllNotes().sortedBy { it.created.date }
                false -> savedStateHandle["notes"] = noteDataSource.getAllNotes().sortedByDescending { it.created.date }
            }
        }
    }

    companion object {
        const val NOTE_KEY = "notes"
        const val SEARCH_TEXT_KEY = "searchText"
        const val IS_SEARCH_ACTIVE_KEY = "isSearchActive"
        const val IS_SORTED_BY_ASCEND = "isSortedByAscend"
    }

}