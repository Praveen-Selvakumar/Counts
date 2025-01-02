package com.example.counts.pages.expense_module

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.counts.R
import com.example.counts.local_db.CountModel
import com.example.counts.local_db.RoomDB
import com.example.counts.local_db.SharedPrefs
import com.example.counts.pages.expenseList_module.ExpenseList

class AddExpense : ComponentActivity() {

    private val REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            if (ContextCompat.checkSelfPermission(
                    this@AddExpense,
                    android.Manifest.permission.RECEIVE_SMS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_SMS,
                        android.Manifest.permission.RECEIVE_SMS
                    ),
                    REQUEST_CODE
                )
            } else {
                Toast.makeText(this, "SMS Tracking", Toast.LENGTH_SHORT).show()
            }

            pageContent(context = this@AddExpense)

        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@AddExpense, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Composable
    fun pageContent(context: AddExpense) {
        var reason by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        var count by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        val savedLimitAmount = SharedPrefs().getFromSharedPreferences(this@AddExpense) as Float

        var amountLimit by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(
                    if (savedLimitAmount > 0.0f
                    ) "${savedLimitAmount} INR" else ""
                )
            )
        }

        var isError: Boolean = false;

        var db = RoomDB.getInstance(context)
        var dao = db?.userDao()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, top = 80.dp, end = 40.dp, bottom = 40.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(30.dp))
                    Text(
                        text = "Add Your Expense",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        style = TextStyle(
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.height(20.dp))
                    TextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        value = reason,
                        shape = RoundedCornerShape(10.dp),
                        isError = isError,

                        onValueChange = {
                            reason = it
                        },
                        label = {
                            Text(
                                "Why You Spent ",
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    Spacer(Modifier.height(20.dp))

                    TextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = count,
                        shape = RoundedCornerShape(10.dp),
                        isError = isError,
                        onValueChange = {
                            count = it
                        },
                        label = {
                            Text(
                                "How much you spent ?",
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = {

                                isError = isFieldEmpty(
                                    reason = reason.text.toString(),
                                    amount = count.text.toString()
                                )

                                if (!isError) {
                                    //ADD
                                    val model = CountModel().apply {
                                        this.amount = count.text.toFloat()
                                        this.why = reason.text.toString()
                                    }

                                    dao?.addExpense(model)
                                    count = count.copy(text = "")
                                    reason = reason.copy(text = "")
                                } else {
                                    Toast.makeText(
                                        this@AddExpense,
                                        "Please fill all details!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Green
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                        ) {
                            Text(text = "ADD EXPENSE")
                        }

                        Button(
                            onClick = {
                                startActivity(Intent(this@AddExpense, ExpenseList::class.java))
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Green
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                        ) {
                            Text(text = "EXPENSE LIST")
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    Divider(
                        color = Color.Gray,       // Divider color
                        thickness = 0.5.dp,       // Reduced thickness to 0.5.dp
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        TextField(
                            modifier = Modifier.weight(1.0f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = amountLimit,
                            shape = RoundedCornerShape(10.dp),
                            isError = isError,
                            onValueChange = {
                                amountLimit = it
                            },
                            label = {
                                Text(
                                    "Enter amount ?",
                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )

                        Button(
                            onClick = {
                                SharedPrefs().saveToSharedPreferences(
                                    this@AddExpense,
                                    amountLimit.text.toFloat()
                                )
                                Toast.makeText(
                                    this@AddExpense,
                                    "Amount Saved\${ amountLimit.text}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Green
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                        ) {
                            Text(text = if (savedLimitAmount > 0.0f) "Save" else "Edit")
                        }

                    }


                }
            }
            cornerDecoration()

        }

    }

    @Composable
    private fun makeLimitNotify(context: Context) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {


        }
    }


    @Composable
    fun cornerDecoration() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 40.dp)
        ) {
            circularImageCard(100.dp, false)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp)
            ) {
                circularImageCard(80.dp, true)
            }
        }
    }


    fun isFieldEmpty(reason: String, amount: String): Boolean {
        if (reason.isNullOrEmpty() || amount.isNullOrEmpty()) return true;
        return false
    }

    @Composable
    fun circularImageCard(size: Dp, isVisible: Boolean) {
        Card(
            elevation = CardDefaults.cardElevation(if (isVisible) 0.dp else 11.dp),
            shape = CircleShape,
            modifier = Modifier
                .size(size),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ) {


            if (isVisible) Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

        }
    }


    @Composable
    fun textField(content: String, isError: Boolean): String {
        var remember by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var fieldData: String = "";
        Column {
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = remember,
                shape = RoundedCornerShape(10.dp),
                onValueChange = {
                    remember = it
                    fieldData = it.toString();
                },
                label = { Text(content) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Please enter, How much you spent!",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }


        return fieldData;
    }


}