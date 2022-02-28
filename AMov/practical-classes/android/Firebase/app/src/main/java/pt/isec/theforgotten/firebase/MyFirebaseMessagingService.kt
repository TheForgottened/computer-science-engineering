package pt.isec.theforgotten.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyService"

    override fun onMessageReceived(rMsg: RemoteMessage) {
        super.onMessageReceived(rMsg)
        if (rMsg.data.size>0) {
            Log.i(TAG, "onMessageReceived - Data: " +
                    "${rMsg.data.toString()}")
        }
        if (rMsg.notification !=null) {
            Log.i(TAG, "onMessageReceived - Notification: " +
                    "${rMsg.notification?.title} "+
                    "${rMsg.notification?.body}")
        }
    }
}