package com.example.counts.SW_component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.example.counts.local_db.CountModel
import com.example.counts.local_db.ExpenseDao
import com.example.counts.local_db.RoomDB
import com.example.counts.local_db.SharedPrefs
import com.example.counts.objects.ConstantsUtils
import com.example.counts.objects.TextUtils


class SMS_Receiver : BroadcastReceiver() {
    private val TAG = "SMS_Receiver"


    var amountPosition = -1;

    var action = -1;

    var amountStr = ""

    var currentAmount = ""


    override fun onReceive(context: Context?, intent: Intent?) {


        if (intent?.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            val extras = intent?.getExtras();
            val objects = extras?.get("pdus") as Array<Any>

            for (item in objects) {
                val msg = SmsMessage.createFromPdu(item as ByteArray)
                var origin: String? = msg.originatingAddress
                var body: String? = msg.messageBody

                val bodyArray = body?.split(" ")

                var amount = "";

                if (!body.isNullOrEmpty()) {
                    if (body.contains("debited")) {
                        try {
                            amount = getProcessedAmount(body).replace(",", "")
                            action = 0
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else if (body.contains("credited")) {
                        try {
                            amount = getProcessedAmount(body).replace(",", "")
                            action = 1
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }/* else {
                        Toast.makeText(context, "No expense added.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onReceive: No expense added.")
                    }*/
                }

                if (action == -1) {
                    Log.d(TAG, " Action is : ${action} and No amount detected!!!")
                }

                Log.d(
                    "//dev",
                    "${action}   ${TextUtils().isNumeric(amount)}  ${!amount.isNullOrEmpty()}"
                )


                if (action >= 0 && TextUtils().isNumeric(amount) && !amount.isNullOrEmpty()) {
                    var model = CountModel(
                        action = action,
                        amount = amount.toFloat(),
                        why = origin.toString()
                    )
                    getDBInstannce(context)?.addExpense(model)
                    ConstantsUtils.needUpdate = true
                    val getSavedAmount =
                        SharedPrefs().getFromSharedPreferences(context as Context) as Float
                    if (getSavedAmount > 0.0f && getSavedAmount < amount.toFloat()) {
                        ConstantsUtils.createNotificationChannel(context)
                        ConstantsUtils.sendNotification(context)
                    }

                    Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show()
                }

            }


        } else if (intent?.getAction().equals("android.provider.Telephony.SMS_SENT")) {
            //do something with the sended sms
        }
    }

    fun getDBInstannce(context: Context?): ExpenseDao {
        //ADD DATA
        var db = RoomDB.getInstance(context!!)
        var dao = db?.userDao()
        return dao!!;
    }

    fun getProcessedAmount(content: String): String {
        val smsBody: List<String> = content.split(" ")
        val identifierList = arrayListOf<String>(
            "rupees",
            "rs.",
            "rs",
            "inr",
            "inr."
        )
        var i = 0;
        while (i < smsBody.size) {
            amountStr = smsBody.get(i).toLowerCase();
            System.out.println(amountStr)
            if (amountStr.contains(".")) {
                amountStr = amountStr.replace(".", " ")
                var innerSplitz = amountStr.split(" ") as List<String>
                var j = 0
                if (identifierList.contains(innerSplitz[0]))
                    Log.d(TAG, "getProcessedAmount: ${currentAmount}")
                while (j < innerSplitz.size) {
                    if (identifierList.contains(innerSplitz[j])) {
                        amountPosition = j + 1;
                        currentAmount = innerSplitz.get(amountPosition)
                        break;
                    }
                    j++;
                }
            } else if (identifierList.contains(amountStr)) {
                amountPosition = i + 1;
                currentAmount = smsBody.get(amountPosition);
                Log.d(TAG, "getProcessedAmount: ${currentAmount}")
                break;
            }
            i++;
        }



        return currentAmount
    }


}