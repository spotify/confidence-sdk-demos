import SwiftUI

extension Color {
    // Material Design inspired colors matching Android app
    static let purple80 = Color(red: 0.82, green: 0.69, blue: 0.84)
    static let purpleGrey80 = Color(red: 0.80, green: 0.74, blue: 0.82)
    static let pink80 = Color(red: 0.95, green: 0.68, blue: 0.84)

    static let purple40 = Color(red: 0.42, green: 0.24, blue: 0.46)
    static let purpleGrey40 = Color(red: 0.38, green: 0.32, blue: 0.40)
    static let pink40 = Color(red: 0.49, green: 0.17, blue: 0.35)
}

struct MaterialDesignTheme {
    let colorScheme: ColorScheme

    var primary: Color {
        colorScheme == .dark ? Color.purple80 : Color.purple40
    }

    var secondary: Color {
        colorScheme == .dark ? Color.purpleGrey80 : Color.purpleGrey40
    }

    var tertiary: Color {
        colorScheme == .dark ? Color.pink80 : Color.pink40
    }

    var surface: Color {
        colorScheme == .dark ? Color(red: 0.1, green: 0.1, blue: 0.1) : Color.white
    }

    var onSurface: Color {
        colorScheme == .dark ? Color.white : Color.black
    }

    var onSurfaceVariant: Color {
        colorScheme == .dark ? Color.gray : Color(red: 0.4, green: 0.4, blue: 0.4)
    }

    var error: Color {
        colorScheme == .dark ? Color.red : Color.red
    }
}

struct MaterialDesignThemeKey: EnvironmentKey {
    static let defaultValue = MaterialDesignTheme(colorScheme: .light)
}

extension EnvironmentValues {
    var materialTheme: MaterialDesignTheme {
        get { self[MaterialDesignThemeKey.self] }
        set { self[MaterialDesignThemeKey.self] = newValue }
    }
}

extension View {
    func materialDesignTheme() -> some View {
        self.environment(\.materialTheme, MaterialDesignTheme(colorScheme: .light))
            .preferredColorScheme(.light)
    }
}