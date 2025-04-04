# :core:notifications module

## Desktop Setup
```kotlin
fun main() = application {
    NotificationInitializer.initialize()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPNotifier Desktop",
    ) {
        println("Desktop app is started")
        App()

    }
}
```

## JS(Web) Setup
```kotlin
fun main()  {
    NotificationInitializer.initialize()
}
```

## Native(iOS) Setup
```kotlin
fun MainViewController() = ComposeUIViewController {
    NotificationInitializer.initialize()
    App()
}
```

## Dependency graph
![Dependency graph](../../docs/images/graphs/dep_graph_core_notifications.svg)
