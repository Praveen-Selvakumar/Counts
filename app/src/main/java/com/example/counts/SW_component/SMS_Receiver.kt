package com.example.counts.SW_component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.example.counts.local_db.CountModel
import com.example.counts.local_db.Dao
import com.example.counts.local_db.RoomDB


class SMS_Receiver : BroadcastReceiver() {


    private val TAG = "SMS_Receiver"

    public var previous  = "";
    public var current  = "";

    override fun onReceive(context: Context?, intent: Intent?) {


        Log.d("//dev", "onReceive: Expense Added!!!")

        if (intent?.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            val extras = intent?.getExtras();
            val objects  = extras?.get("pdus") as Array<Any>

            for(item in  objects){
                val  msg = SmsMessage.createFromPdu(item as ByteArray)
                var origin  : String? = msg.originatingAddress
                var body  : String? = msg.messageBody

                val bodyArray   =    body?.split(" ")

                var amount = "";
                var action = 0;

                if (body?.contains("debited")!!){
                    amount =  bodyArray?.get(4).toString()
                    action = 0;
                }else if(body.contains("credited")){
                    amount =  bodyArray?.get(0).toString()
                    val  _amount  = amount.split(".")
                    amount = _amount.get(1);
                    action = 1;
                }
                Log.d("//DEV", "Amount Received"+amount)
                  //ADD
                var model = CountModel(
                    action = action,
                    amount = amount.toFloat() ,
                    why = origin.toString()
                )
                getDBInstannce(context)?.addExpense(model)
                Toast.makeText(context, "Expense Added", Toast.LENGTH_SHORT).show()
            }


        }else  if(intent?.getAction().equals("android.provider.Telephony.SMS_SENT")){
            //do something with the sended sms
        }
    }

     fun getDBInstannce(context: Context?): Dao{
         //ADD DATA
         var db  = RoomDB.getInstance(context!!)
         var dao = db?.userDao()
         return  dao!!;
     }





}