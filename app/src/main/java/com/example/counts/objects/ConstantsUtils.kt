package com.example.counts.objects

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.counts.R
import com.example.counts.SW_component.SMS_Receiver
import com.example.counts.pages.expense_module.AddExpense

object ConstantsUtils {

    val TAG = "ConstantsUtils"

    var needUpdate = false;


    val COUNT_LIMIT: String? = "COUNT_LIMIT"


    val getFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Light),
        Font(R.font.poppins_medium, FontWeight.Medium),
    )


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context) {
        val channelId = "my_channel_id"
        val notificationId = 1

        // Intent to open an activity when the notification is clicked
        val intent = Intent(context, AddExpense::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.coin) // Replace with your drawable icon
            .setContentTitle("Alert!!!")
            .setContentText("Your Expenditure is beyound limit , stop spending")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(notificationId, builder.build())
            } else {
                Toast.makeText(
                    context,
                    "Notification permission not allowed!!!!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "sendNotification: Permission not allowed ")
            }
        }
    }

}