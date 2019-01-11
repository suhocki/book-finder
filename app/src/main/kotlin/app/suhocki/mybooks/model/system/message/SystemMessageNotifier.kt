package app.suhocki.mybooks.model.system.message

class SystemMessageNotifier {
    var notificationReceiver: ((SystemMessage) -> Unit)? = null

    fun send(message: SystemMessage) = notificationReceiver?.invoke(message)
    fun send(message: String) = notificationReceiver?.invoke(SystemMessage(message))
}