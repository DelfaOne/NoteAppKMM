//
//  HideableSearchTextField.swift
//  iosApp
//
//  Created by Fadel Foudi on 25/02/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct HideableSearchTextField<Destination: View>: View {
    
    var onSearToggled: () -> Void
    var destinationProvider: () -> Destination
    var isSearchActive: Bool
    @Binding var searchText: String
    
    var body: some View {
        HStack {
            TextField("Search...", text: $searchText)
                .textFieldStyle(.roundedBorder)
                .opacity(isSearchActive ? 1 : 0)
            if !isSearchActive {
                Spacer()
            }
            
            Button(action: onSearToggled) {
                Image(systemName: isSearchActive ? "xmark" : "magnifyingglass")
            }
            NavigationLink(destination: destinationProvider) {
                Image(systemName: "plus")
            }
        }
    }
}

#Preview {
    HideableSearchTextField(
        onSearToggled: {}, destinationProvider: {EmptyView()}, isSearchActive: true, searchText: .constant("Hello world"))
}
