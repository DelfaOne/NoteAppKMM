import SwiftUI
import shared

@main
struct iOSApp: App {
    
    private let dataBaseModule = DatabaseModule()
	var body: some Scene {
		WindowGroup {
            NavigationView {
                NoteListScreen(noteDataSource:  dataBaseModule.noteDataSource)
            }.accentColor(.black)
		}
	}
}
