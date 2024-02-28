//
//  NoteListScreen.swift
//  iosApp
//
//  Created by Fadel Foudi on 23/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct NoteListScreen: View {
    private var noteDataSource: NoteDataSource
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil)
    
    @State private var isNoteSelected = false
    @State private var selectedNoteId: Int64? = nil
    
    init(noteDataSource: NoteDataSource) {
        self.noteDataSource = noteDataSource
    }
    var body: some View {
        VStack {
            ZStack {
                NavigationLink(
                    destination: NoteDetailScreen(noteDataSource: self.noteDataSource, noteId: selectedNoteId),
                    isActive: $isNoteSelected,
                    label: {
                        EmptyView()
                    }
                )
                .hidden()
                
                HideableSearchTextField<NoteDetailScreen>(
                    onSearchToggled: {viewModel.toggleIsSearchActive()},
                    destinationProvider: { NoteDetailScreen(noteDataSource: noteDataSource, noteId: selectedNoteId) },
                    isSearchActive: viewModel.isSearchActive,
                    searchText: $viewModel.searchText
                )
                .frame(minWidth: 40, maxWidth: .infinity)
                .padding()
                
                if !viewModel.isSearchActive {
                    Text("All notes")
                        .font(.title2)
                }
            }
            List {
                ForEach(viewModel.filteredNotes, id: \.self.id) { note in
                    Button(action: {
                        isNoteSelected = true
                        selectedNoteId = note.id?.int64Value
                    }) {
                        NoteItem(note: note, onDeleteClick: {
                            viewModel.deleteNoteById(id: note.id?.int64Value ?? -1)
                        })
                    }.listRowSeparator(.hidden)
                }
            }
            .onAppear {
                viewModel.loadNotes()
            }
            .listRowSeparator(.hidden)
            .listStyle(.plain)
            
        }
        .onAppear {
            selectedNoteId = nil
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
    }
}

#Preview {
    EmptyView()
}
